#!/bin/sh

# Wait for DynamoDB Local to be available
until curl -s http://dynamodb-local:8000; do
  echo "Waiting for dynamodb-local to be available..."
  sleep 2
done

# Configure AWS CLI
aws configure set aws_access_key_id dummy --profile local
aws configure set aws_secret_access_key dummy --profile local
aws configure set region us-east-1 --profile local

# Create the table based on the JSON file
echo "Creating table..."
aws dynamodb create-table --cli-input-json file:///scripts/create-table.json --endpoint-url http://dynamodb-local:8000 --region us-east-1 --profile local

# Wait for table to be created
echo "Waiting for table to be created..."
aws dynamodb wait table-exists --table-name Tasks --endpoint-url http://dynamodb-local:8000 --region us-east-1 --profile local

# Batch write items to the table
echo "Batch writing items..."
aws dynamodb batch-write-item --request-items file:///scripts/batch-write-items.json --endpoint-url http://dynamodb-local:8000 --region us-east-1 --profile local

# List tables to confirm creation
echo "Listing tables..."
aws dynamodb list-tables --endpoint-url http://dynamodb-local:8000 --region us-east-1 --profile local

# Scan table to confirm items
echo "Scanning table to confirm items..."
aws dynamodb scan --table-name Tasks --endpoint-url http://dynamodb-local:8000 --region us-east-1 --profile local
