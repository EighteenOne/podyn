/**
 * 
 */
package com.citusdata.migration.compatibility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.dynamodbv2.waiters.AmazonDynamoDBWaiters;
import com.citusdata.migration.datamodel.TableSchema;

/**
 * @author marco
 *
 */
public class PostgresDynamoDB implements AmazonDynamoDB {

	final Connection connection;
	final Map<String,TableSchema> schemaCache;
	
	public PostgresDynamoDB(String postgresJDBCURL) throws SQLException {
		this.connection = DriverManager.getConnection(postgresJDBCURL);
		this.schemaCache = new HashMap<>();
	}

	@Override
	public void setEndpoint(String endpoint) {
	}

	@Override
	public void setRegion(Region region) {
		
		
	}

	@Override
	public BatchExecuteStatementResult batchExecuteStatement(BatchExecuteStatementRequest batchExecuteStatementRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchGetItemResult batchGetItem(BatchGetItemRequest batchGetItemRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchWriteItemResult batchWriteItem(BatchWriteItemRequest batchWriteItemRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BatchWriteItemResult batchWriteItem(Map<String, List<WriteRequest>> requestItems) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateBackupResult createBackup(CreateBackupRequest createBackupRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateGlobalTableResult createGlobalTable(CreateGlobalTableRequest createGlobalTableRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CreateTableResult createTable(CreateTableRequest createTableRequest) {
	    throw new UnsupportedOperationException();
	}

	@Override
	public CreateTableResult createTable(List<AttributeDefinition> attributeDefinitions, String tableName,
			List<KeySchemaElement> keySchema, ProvisionedThroughput provisionedThroughput) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteBackupResult deleteBackup(DeleteBackupRequest deleteBackupRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteItemResult deleteItem(DeleteItemRequest deleteItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteTableResult deleteTable(DeleteTableRequest deleteTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteTableResult deleteTable(String tableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeBackupResult describeBackup(DescribeBackupRequest describeBackupRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeContinuousBackupsResult describeContinuousBackups(DescribeContinuousBackupsRequest describeContinuousBackupsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeContributorInsightsResult describeContributorInsights(DescribeContributorInsightsRequest describeContributorInsightsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeEndpointsResult describeEndpoints(DescribeEndpointsRequest describeEndpointsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeExportResult describeExport(DescribeExportRequest describeExportRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeGlobalTableResult describeGlobalTable(DescribeGlobalTableRequest describeGlobalTableRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeGlobalTableSettingsResult describeGlobalTableSettings(DescribeGlobalTableSettingsRequest describeGlobalTableSettingsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeKinesisStreamingDestinationResult describeKinesisStreamingDestination(DescribeKinesisStreamingDestinationRequest describeKinesisStreamingDestinationRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeLimitsResult describeLimits(DescribeLimitsRequest describeLimitsRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTableResult describeTable(DescribeTableRequest describeTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTableResult describeTable(String tableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTableReplicaAutoScalingResult describeTableReplicaAutoScaling(DescribeTableReplicaAutoScalingRequest describeTableReplicaAutoScalingRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DescribeTimeToLiveResult describeTimeToLive(DescribeTimeToLiveRequest describeTimeToLiveRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public DisableKinesisStreamingDestinationResult disableKinesisStreamingDestination(DisableKinesisStreamingDestinationRequest disableKinesisStreamingDestinationRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public EnableKinesisStreamingDestinationResult enableKinesisStreamingDestination(EnableKinesisStreamingDestinationRequest enableKinesisStreamingDestinationRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExecuteStatementResult executeStatement(ExecuteStatementRequest executeStatementRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExecuteTransactionResult executeTransaction(ExecuteTransactionRequest executeTransactionRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExportTableToPointInTimeResult exportTableToPointInTime(ExportTableToPointInTimeRequest exportTableToPointInTimeRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(GetItemRequest getItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(String tableName, Map<String, AttributeValue> key) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public GetItemResult getItem(String tableName, Map<String, AttributeValue> key, Boolean consistentRead) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListBackupsResult listBackups(ListBackupsRequest listBackupsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListContributorInsightsResult listContributorInsights(ListContributorInsightsRequest listContributorInsightsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListExportsResult listExports(ListExportsRequest listExportsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListGlobalTablesResult listGlobalTables(ListGlobalTablesRequest listGlobalTablesRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(ListTablesRequest listTablesRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables() {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(String exclusiveStartTableName) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(String exclusiveStartTableName, Integer limit) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTablesResult listTables(Integer limit) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ListTagsOfResourceResult listTagsOfResource(ListTagsOfResourceRequest listTagsOfResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(PutItemRequest putItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(String tableName, Map<String, AttributeValue> item) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public PutItemResult putItem(String tableName, Map<String, AttributeValue> item, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public QueryResult query(QueryRequest queryRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public RestoreTableFromBackupResult restoreTableFromBackup(RestoreTableFromBackupRequest restoreTableFromBackupRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RestoreTableToPointInTimeResult restoreTableToPointInTime(RestoreTableToPointInTimeRequest restoreTableToPointInTimeRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(ScanRequest scanRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, List<String> attributesToGet) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, Map<String, Condition> scanFilter) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public ScanResult scan(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public TagResourceResult tagResource(TagResourceRequest tagResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public TransactGetItemsResult transactGetItems(TransactGetItemsRequest transactGetItemsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TransactWriteItemsResult transactWriteItems(TransactWriteItemsRequest transactWriteItemsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UntagResourceResult untagResource(UntagResourceRequest untagResourceRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateContinuousBackupsResult updateContinuousBackups(UpdateContinuousBackupsRequest updateContinuousBackupsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateContributorInsightsResult updateContributorInsights(UpdateContributorInsightsRequest updateContributorInsightsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateGlobalTableResult updateGlobalTable(UpdateGlobalTableRequest updateGlobalTableRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateGlobalTableSettingsResult updateGlobalTableSettings(UpdateGlobalTableSettingsRequest updateGlobalTableSettingsRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(UpdateItemRequest updateItemRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(
			String tableName,
			Map<String, AttributeValue> key,
			Map<String, AttributeValueUpdate> attributeUpdates) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key,
			Map<String, AttributeValueUpdate> attributeUpdates, String returnValues) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTableResult updateTable(UpdateTableRequest updateTableRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTableResult updateTable(String tableName, ProvisionedThroughput provisionedThroughput) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTableReplicaAutoScalingResult updateTableReplicaAutoScaling(UpdateTableReplicaAutoScalingRequest updateTableReplicaAutoScalingRequest) {
		throw new UnsupportedOperationException();
	}

	@Override
	public UpdateTimeToLiveResult updateTimeToLive(UpdateTimeToLiveRequest updateTimeToLiveRequest) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutdown() {
		try {
			connection.close();
		} catch (SQLException e) {
		}
	}

	@Override
	public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
		
		throw new UnsupportedOperationException();
	}

	@Override
	public AmazonDynamoDBWaiters waiters() {
		throw new UnsupportedOperationException();
	}


}
