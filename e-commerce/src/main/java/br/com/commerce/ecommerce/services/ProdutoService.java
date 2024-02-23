package br.com.commerce.ecommerce.services;


import java.util.List;

import br.com.commerce.ecommerce.dtos.ProdutoDto;

public interface ProdutoService {
    ProdutoDto createProdutoService(ProdutoDto produtoDto);
    ProdutoDto findProdutoByIdService(Long id);
    List<ProdutoDto> findAllProdutosService();
    ProdutoDto updateProdutoService(Long id, ProdutoDto produtoDto);
    void removeProdutoService(Long id);
}
