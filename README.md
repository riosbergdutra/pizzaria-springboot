# pizzaria-springboot

# para o aws cli com localstack usuarios
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name usuarios \
    --attribute-definitions \
        AttributeName=id,AttributeType=N \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para o aws cli com localstack produtos
aws --endpoint-url=http://localhost:4566 dynamodb create-table \
    --table-name produtos \
    --attribute-definitions \
        AttributeName=id,AttributeType=N \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

# para poder visualizar 
aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name usuarios

aws --endpoint-url=http://localhost:4566 dynamodb scan --table-name produtos

# salvando para depois
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    categoria ENUM('pizza', 'bebida', 'sobremesa', 'outro') NOT NULL,
    codigo_de_barras VARCHAR(255) UNIQUE NOT NULL,
    imagem LONGBLOB
);

# minha fila de SQS aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name minha-fila
