CREATE TABLE usuarios (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_completo VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    endereco VARCHAR(255),
    numero_telefone VARCHAR(20),
    data_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(50) DEFAULT 'user'
);
