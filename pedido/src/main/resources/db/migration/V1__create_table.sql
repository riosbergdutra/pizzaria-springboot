CREATE TABLE pedido (
    pedido_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    quantidade INT,
    endereco_entrega VARCHAR(255),
    numero_telefone_entrega VARCHAR(20),
    frete DECIMAL(10, 2),
    total_pedido DECIMAL(10, 2),
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_pedido VARCHAR(50) DEFAULT 'pendente',
    FOREIGN KEY (user_id) REFERENCES usuarios(user_id),
    FOREIGN KEY (product_id) REFERENCES produto(product_id)
);
