CREATE TABLE carrinho (
    carrinho_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_Id BIGINT,
    product_Id BIGINT,
    quantidade INT,
    preco_total DECIMAL(10, 2),
    frete DECIMAL(10, 2),
    FOREIGN KEY (user_Id) REFERENCES pizzaria_usuarios.usuarios(user_Id),
    FOREIGN KEY (product_Id) REFERENCES pizzaria_produtos.produto(product_Id)
);
