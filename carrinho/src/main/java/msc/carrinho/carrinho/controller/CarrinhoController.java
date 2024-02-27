package msc.carrinho.carrinho.controller;

import msc.carrinho.carrinho.dtos.CarrinhoDto;
import msc.carrinho.carrinho.services.CarrinhoCacheService;
import msc.carrinho.carrinho.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private CarrinhoCacheService carrinhoCacheService;

    @PostMapping
    public ResponseEntity<CarrinhoDto> createCarrinho(@RequestBody CarrinhoDto carrinhoDto) {
        CarrinhoDto novoCarrinho = carrinhoService.createCarrinhoService(carrinhoDto);
        carrinhoCacheService.cacheCarrinho(novoCarrinho.getCarrinho_id().toString(), novoCarrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCarrinho);
    }

    @Cacheable(value = "carrinho", key = "#carrinho_id")
    @GetMapping("/{carrinho_id}")
    public ResponseEntity<CarrinhoDto> findCarrinhoById(@PathVariable Long carrinho_id) {
        CarrinhoDto carrinhoDto = carrinhoCacheService.getCarrinhoFromCache(carrinho_id.toString());
        if (carrinhoDto != null) {
            return ResponseEntity.ok(carrinhoDto);
        } else {
            carrinhoDto = carrinhoService.findCarrinhoByIdService(carrinho_id);
            if (carrinhoDto != null) {
                carrinhoCacheService.cacheCarrinho(carrinho_id.toString(), carrinhoDto);
                return ResponseEntity.ok(carrinhoDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }    

    @GetMapping
    public ResponseEntity<List<CarrinhoDto>> findAllCarrinhos() {
        List<CarrinhoDto> carrinhos = carrinhoService.findAllCarrinhoService();
        return ResponseEntity.ok(carrinhos);
    }

    @CachePut(value = "carrinho", key = "#carrinho_id")
    @PutMapping("/{carrinho_id}")
    public ResponseEntity<CarrinhoDto> updateCarrinho(@PathVariable Long carrinho_id, @RequestBody CarrinhoDto carrinhoDto) {
        CarrinhoDto carrinhoAtualizado = carrinhoService.updateCarrinhoService(carrinho_id, carrinhoDto);
        if (carrinhoAtualizado != null) {
            return ResponseEntity.ok(carrinhoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CacheEvict(value = "carrinho", key = "#carrinho_id")
    @DeleteMapping("/{carrinho_id}")
    public ResponseEntity<Void> removeCarrinho(@PathVariable Long carrinho_id) {
        carrinhoService.removeCarrinhoService(carrinho_id);
        carrinhoCacheService.removeCarrinhoFromCache(carrinho_id.toString()); // Remover do cache
        return ResponseEntity.noContent().build();
    }
}
