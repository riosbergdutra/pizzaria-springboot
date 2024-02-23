package br.com.commerce.ecommerce.controllers;

import br.com.commerce.ecommerce.dtos.ProdutoDto;
import br.com.commerce.ecommerce.services.ProdutoCacheService;
import br.com.commerce.ecommerce.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ProdutoCacheService produtoCacheService;

    @PostMapping
    public ResponseEntity<ProdutoDto> createProdutoController(@RequestBody ProdutoDto produtoDto) {
        ProdutoDto novoProduto = produtoService.createProdutoService(produtoDto);
        produtoCacheService.cacheProduto(novoProduto.getId().toString(), novoProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProduto);
    }

    @Cacheable("produtos")
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> findProdutoByIdController(@PathVariable Long id) {
        ProdutoDto produtoDto = produtoCacheService.getProdutoFromCache(id.toString());
        if (produtoDto != null) {
            return ResponseEntity.ok(produtoDto);
        } else {
            produtoDto = produtoService.findProdutoByIdService(id);
            if (produtoDto != null) {
                produtoCacheService.cacheProduto(id.toString(), produtoDto); 
                return ResponseEntity.ok(produtoDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
    

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> findAllProdutosController() {
        List<ProdutoDto> produtos = produtoService.findAllProdutosService();
        return ResponseEntity.ok(produtos);
    }

    @CachePut(value = "produtos", key = "#id")
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> updateProdutoController(@PathVariable Long id, @RequestBody ProdutoDto produtoDto) {
        ProdutoDto produtoAtualizado = produtoService.updateProdutoService(id, produtoDto);
        if (produtoAtualizado != null) {
            return ResponseEntity.ok(produtoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CacheEvict(value = "produtos", key = "#id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeProdutoController(@PathVariable Long id) {
        produtoService.removeProdutoService(id);
        produtoCacheService.removeProdutoFromCache(id.toString()); // Remover do cache
        return ResponseEntity.noContent().build();
    }
}
