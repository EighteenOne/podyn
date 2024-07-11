/**
 * 
 */
package com.citusdata.migration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreams;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBStreamsClientBuilder;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.citusdata.migration.datamodel.NonExistingTableException;
import com.citusdata.migration.datamodel.TableEmitter;
import com.citusdata.migration.datamodel.TableExistsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author marco
 *
 */
@Slf4j
public class DynamoDBReplicator {
	public static void main(String[] args) throws SQLException, IOException {
		Options options = new Options();

		Option help = new Option("h", "help", false, "Show help");
		help.setRequired(false);
		options.addOption(help);

		Option dynamoDBTableOption = new Option("t", "table", true, "DynamoDB table name(s) to replicate");
		dynamoDBTableOption.setRequired(false);
		options.addOption(dynamoDBTableOption);

        Option postgresTableOption = new Option("pt", "postgres-table", true, "Postgres table name(s) to target");
		postgresTableOption.setRequired(false);
		options.addOption(postgresTableOption);

		Option postgresURLOption = new Option("u", "postgres-jdbc-url", true, "PostgreSQL JDBC URL of the destination");
		postgresURLOption.setRequired(false);
		options.addOption(postgresURLOption);

		Option noSchemaOption = new Option("s", "schema", false, "Replicate the table schema");
		noSchemaOption.setRequired(false);
		options.addOption(noSchemaOption);

		Option noDataOption = new Option("d", "data", false, "Replicate the current data");
		noDataOption.setRequired(false);
		options.addOption(noDataOption);

		Option noChangesOption = new Option("c", "changes", false, "Continuously replicate changes");
		noChangesOption.setRequired(false);
		options.addOption(noChangesOption);

		Option citusOption = new Option("x", "citus", false, "Create distributed tables using Citus");
		citusOption.setRequired(false);
		options.addOption(citusOption);

		Option conversionModeOption = new Option("m", "conversion-mode", true, "Conversion mode, either columns or jsonb (default: columns)");
		conversionModeOption.setRequired(false);
		options.addOption(conversionModeOption);

		Option lowerCaseColumnsOption = new Option("lc", "lower-case-column-names", false, "Use lower case column names");
		lowerCaseColumnsOption.setRequired(false);
		options.addOption(lowerCaseColumnsOption);

		Option numConnectionsOption = new Option("n", "num-connections", true, "Database connection pool size (default 16)");
		numConnectionsOption.setRequired(false);
		options.addOption(numConnectionsOption);

		Option maxScanRateOption = new Option("r", "scan-rate", true, "Maximum reads/sec during scan (default 25)");
		maxScanRateOption.setRequired(false);
		options.addOption(maxScanRateOption);

		Option numWorkers = new Option("dw", "num-data-workers", true, "Number of workers to use for performing the data operation.");
		numWorkers.setRequired(false);
		options.addOption(numWorkers);

		Option scanLimit = new Option("sl", "scan-limit", true, "Maximum number of items to retrieve in individual scans to help control throughput footprint.");
		scanLimit.setRequired(false);
		options.addOption(scanLimit);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(120);

		try {
			CommandLine cmd = parser.parse(options, args);

			boolean wantHelp = cmd.hasOption("help");
			boolean replicateSchema = cmd.hasOption("schema");
			boolean replicateData = cmd.hasOption("data");
			boolean replicateChanges = cmd.hasOption("changes");

			if (wantHelp || (!replicateSchema && !replicateData && !replicateChanges)) {
				formatter.printHelp("podyn", options);
				return;
			}

			boolean useCitus = cmd.hasOption("citus");
			boolean useLowerCaseColumnNames = cmd.hasOption("lower-case-column-names");
			int maxScanRate = Integer.parseInt(cmd.getOptionValue("scan-rate", "25"));
			int dbConnectionCount = Integer.parseInt(cmd.getOptionValue("num-connections", "16"));
            String dynamoTableNamesString = cmd.getOptionValue("table");
			String postgresTableNamesString = cmd.getOptionValue("postgres-table");
			String postgresURL = cmd.getOptionValue("postgres-jdbc-url");
			String conversionModeString = cmd.getOptionValue("conversion-mode", ConversionMode.columns.name());

			final int dataWorkers = Integer.parseInt(cmd.getOptionValue("num-data-workers", "1"));
			int scanLimitSetting = Integer.parseInt(cmd.getOptionValue("scan-limit", "100"));
			if(scanLimitSetting < 50){
				log.warn("{} provided for scan-limit, 50 is the minimum value allowed.", scanLimitSetting);
				scanLimitSetting = 50;
			}
			ConversionMode conversionMode;

			try {
				conversionMode = ConversionMode.valueOf(conversionModeString);
			} catch (IllegalArgumentException e) {
				throw new ParseException("invalid conversion mode: " + conversionModeString);
			}

			AWSCredentialsProvider credentialsProvider = new DefaultAWSCredentialsProviderChain();

			AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().
					withCredentials(credentialsProvider).
					build();

			AmazonDynamoDBStreams streamsClient = AmazonDynamoDBStreamsClientBuilder.standard().
					withCredentials(credentialsProvider).
					build();

			final TableEmitter emitter;

			if (postgresURL != null) {
				List<TableEmitter> emitters = new ArrayList<>();

				for(int i = 0; i < dbConnectionCount; i++) {
					emitters.add(new JDBCTableEmitter(postgresURL));
				}

				emitter = new HashedMultiEmitter(emitters);
			} else {
				emitter = new StdoutSQLEmitter();
			}

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					log.info("Closing database connections");
					emitter.close();
				}
			});

			List<DynamoDBTableReplicator> replicators = new ArrayList<>();

			List<String> dynamoTableNames = new ArrayList<>();
			List<String> postgresTableNames = new ArrayList<>();
			HashMap<String, String> tableLookup = new HashMap<>();

			if (dynamoTableNamesString != null) {
				dynamoTableNames = Arrays.asList(dynamoTableNamesString.split(","));
				if (postgresTableNamesString != null) {
					postgresTableNames = Arrays.asList(postgresTableNamesString.split(","));
					if (postgresTableNames.size() != dynamoTableNames.size()) {
						throw new ParseException("Dynamo and Postgres table lists must be the same size."
						+ "pt str: "+ postgresTableNamesString + " size " + postgresTableNames.size() + "list:" + postgresTableNames + ";"
						+ "dynamo str:" + dynamoTableNamesString + " size " + dynamoTableNames.size() + "list:" + dynamoTableNames);
					}
				}
			    for(int i = 0; i < dynamoTableNames.size(); i++) {
					tableLookup.put(dynamoTableNames.get(i), postgresTableNames.get(i));
				}
			} else {
				ListTablesResult listTablesResult = dynamoDBClient.listTables();
				for(String tableName : listTablesResult.getTableNames()) {
					if (!tableName.startsWith(DynamoDBTableReplicator.LEASE_TABLE_PREFIX)) {
						dynamoTableNames.add(tableName);
					}
				}
			}

			ExecutorService executor = Executors.newCachedThreadPool();

			for(String dynamoTableName : dynamoTableNames) {
				String postgresTableName = tableLookup.get(dynamoTableName);
				// If we don't have a postgres table name,
				// use the dynamo table name
				if (postgresTableName.isEmpty()) {
					postgresTableName = dynamoTableName;
				}
				DynamoDBTableReplicator replicator = new DynamoDBTableReplicator(
						dynamoDBClient, streamsClient, credentialsProvider, executor, emitter, dynamoTableName, postgresTableName);

				replicator.setAddColumnEnabled(true);
				replicator.setUseCitus(useCitus);
				replicator.setUseLowerCaseColumnNames(useLowerCaseColumnNames);
				replicator.setConversionMode(conversionMode);

				replicators.add(replicator);
			}

			if (replicateSchema) {
				for(DynamoDBTableReplicator replicator : replicators) {
					log.info(String.format("Constructing table schema for dynamo table %s into postgres table %s", replicator.dynamoTableName, replicator.postgresTableName));

					replicator.replicateSchema();
				}
			}

			if (replicateData) {
				List<Future<Long>> futureResults = new ArrayList<Future<Long>>();

				for(DynamoDBTableReplicator replicator : replicators) {
					log.info("Replicating data for table {} for {} workers, using {} for the scan limit.", replicator.dynamoTableName, dataWorkers, scanLimitSetting);
					List<Future<Long>> replicatorFutures = replicator.startReplicatingData(maxScanRate, dataWorkers, scanLimitSetting);
					futureResults.addAll(replicatorFutures);
				}

				for(Future<Long> futureResult : futureResults) {
					futureResult.get();
				}
			}

			if (replicateChanges) {
				for(DynamoDBTableReplicator replicator : replicators) {
					log.info("Replicating changes for table {}", replicator.dynamoTableName);
					replicator.startReplicatingChanges();
				}
			} else {
				executor.shutdown();
			}

		} catch (ParseException e) {
			log.error(e.getMessage());
			formatter.printHelp("podyn", options);
			System.exit(3);
		} catch (TableExistsException|NonExistingTableException e) {
			log.error(e.getMessage());
			System.exit(1);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();

			if (cause.getCause() != null) {
				log.error(cause.getCause().getMessage());
			} else {
				log.error(cause.getMessage());
			}
			System.exit(1);
		} catch (EmissionException e) {
			if (e.getCause() != null) {
				log.error(e.getCause().getMessage());
			} else {
				log.error(e.getMessage());
			}
			System.exit(1);
		} catch (RuntimeException e) {
			log.error("A RuntimeException occurred.", e);
			System.exit(2);
		} catch (Exception e) {
			log.error("A Exception occurred.", e);
			System.exit(1);
		}
	}

}
