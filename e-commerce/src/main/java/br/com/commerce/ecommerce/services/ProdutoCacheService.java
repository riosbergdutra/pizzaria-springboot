package br.com.commerce.ecommerce.services;

import br.com.commerce.ecommerce.dtos.ProdutoDto;

public interface ProdutoCacheService {
    void cacheProduto(String key, ProdutoDto produtoDto);
    ProdutoDto getProdutoFromCache(String id);
    void removeProdutoFromCache(String id);
    void updateProdutoInCache(String key, ProdutoDto produtoDto);
}
