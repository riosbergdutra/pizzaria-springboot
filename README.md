# pizzaria-springboot

# para o aws cli com localstack 
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name usuarios \
    --attribute-definitions \
        AttributeName=id,AttributeType=N \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
        AttributeName=email,KeyType=RANGE \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5
