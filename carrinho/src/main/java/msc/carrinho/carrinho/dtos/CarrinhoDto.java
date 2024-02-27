package msc.carrinho.carrinho.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import msc.carrinho.carrinho.model.Carrinho;

public record CarrinhoDto(
    Long carrinho_id,
    Long user_Id,
    Long product_Id,
    int quantidade,
    double preco_total,
    double frete
) {
    public CarrinhoDto(Carrinho novoCarrinho) {
        this(
            novoCarrinho.getCarrinho_id(),
            novoCarrinho.getUser_Id(),
            novoCarrinho.getProduct_Id(),
            novoCarrinho.getQuantidade(),
            novoCarrinho.getPreco_total(),
            novoCarrinho.getFrete()
        );
    }

    public Long getCarrinho_id() {
        return carrinho_id;
    }
    
     public Long getUser_Id() {
        return user_Id;
    }

     public Long getProduct_Id() {
        return product_Id;
    }

     public int getQuantidade() {
        return quantidade;
    }

     public double getPreco_total() {
        return preco_total;
    }

     public double getFrete() {
        return frete;
    }

      public static CarrinhoDto fromJson(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("A string JSON fornecida está vazia ou nula");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, CarrinhoDto.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao converter JSON para ProdutoDto", e);
        }
    }

    public String toJson() {  
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter ProdutoDto para JSON", e);
        }
    }
}
