package br.com.commerce.ecommerce.services;


import java.util.List;

import br.com.commerce.ecommerce.dtos.ProdutoDto;

public interface ProdutoService {
    ProdutoDto createProdutoService(ProdutoDto produtoDto);
    ProdutoDto findProdutoByIdService(Long product_id);
    List<ProdutoDto> findAllProdutosService();
    ProdutoDto updateProdutoService(Long product_id, ProdutoDto produtoDto);
    void removeProdutoService(Long product_id);
}
