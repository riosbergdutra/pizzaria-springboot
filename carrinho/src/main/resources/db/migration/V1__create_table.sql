CREATE TABLE pizzaria_carrinho (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    userId BIGINT,
    productId BIGINT,
    quantity INT,
    preco_total DECIMAL(10, 2),
    frete DECIMAL(10, 2),
    FOREIGN KEY (userId) REFERENCES pizzaria_usuarios.usuarios(id),
    FOREIGN KEY (productId) REFERENCES pizzaria.produto(id)
);
