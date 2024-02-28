CREATE TABLE pedidos (
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
    FOREIGN KEY (user_id) REFERENCES pizzaria_usuarios.usuarios(user_Id),
    FOREIGN KEY (product_id) REFERENCES pizzaria_produtos.produto(product_Id)
);
