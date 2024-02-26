package br.com.commerce.ecommerce.dtos;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.commerce.ecommerce.enums.Categoria;
import br.com.commerce.ecommerce.models.Produto;

public record ProdutoDto(
    Long product_id,
    String nome,
    String descricao,
    double preco_unitario,
    String imagem,
    int codigo_barra,
    Categoria categoria
) { 
    public ProdutoDto(Produto novoProduto) {
        this(
            novoProduto.getProduct_id(),
            novoProduto.getNome(),
            novoProduto.getDescricao(),
            novoProduto.getPreco_unitario(),
            novoProduto.getImagem(),
            novoProduto.getCodigo_barra(),
            novoProduto.getCategoria()
        );
    }

    public Long getProduct_id() {
        return product_id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPreco_unitario() {
        return preco_unitario;
    }

    public String getImagem() {
        return imagem;
    }

    public int getCodigo_barra() {
        return codigo_barra;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }



    public static ProdutoDto fromJson(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("A string JSON fornecida está vazia ou nula");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, ProdutoDto.class);
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
