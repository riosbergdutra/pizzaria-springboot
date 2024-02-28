# pizzaria-springboot

# para o aws cli com localstack usuarios
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name usuarios \
    --attribute-definitions \
        AttributeName=user_id,AttributeType=N \
    --key-schema \
        AttributeName=user_id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para o aws cli com localstack produtos
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name produtos \
    --attribute-definitions \
        AttributeName=product_id,AttributeType=N \
    --key-schema \
        AttributeName=product_id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para o aws cli com localstack carrinho
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name carrinho \
    --attribute-definitions \
        AttributeName=carrinho_id,AttributeType=N \
    --key-schema \
        AttributeName=carrinho_id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para o aws cli com localstack pedidos

ws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name pedidos \
    --attribute-definitions \
        AttributeName=pedido_id,AttributeType=N \
    --key-schema \
        AttributeName=pedido_id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para poder visualizar 
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name usuarios

aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name produtos

aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name carrinho

aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name pedidos


# minha fila de SQS
 aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name minha-fila
