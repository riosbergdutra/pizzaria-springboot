CREATE TABLE produto (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    preco_unitario DOUBLE NOT NULL,
    imagem VARCHAR(255) NOT NULL,
    codigo_barra INT NOT NULL UNIQUE,
    categoria VARCHAR(255) NOT NULL
);
