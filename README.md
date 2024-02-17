# pizzaria-springboot

# para o aws cli com localstack 
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name usuarios \
    --attribute-definitions \
        AttributeName=id,AttributeType=N \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5
# para poder visualizar 
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name usuarios