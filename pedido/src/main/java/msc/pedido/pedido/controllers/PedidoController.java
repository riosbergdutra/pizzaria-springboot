package msc.pedido.pedido.controllers;

import msc.pedido.pedido.dtos.PedidoDto;
import msc.pedido.pedido.services.PedidoCacheService;
import msc.pedido.pedido.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoCacheService pedidoCacheService;

    @PostMapping
    public ResponseEntity<PedidoDto> criarPedido(@RequestBody PedidoDto pedidoDto) {
        PedidoDto novoPedido = pedidoService.criarPedido(pedidoDto);
        pedidoCacheService.cachePedido(String.valueOf(novoPedido.getPedido_id()), novoPedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }
    
    
    @Cacheable("pedidos")
    @GetMapping("/{pedido_id}")
    public ResponseEntity<PedidoDto> getPedidoById(@PathVariable long pedido_id) {
        PedidoDto pedidoDto = pedidoCacheService.getPedidoFromCache(String.valueOf(pedido_id));
        if (pedidoDto != null) {
            return ResponseEntity.ok(pedidoDto);
        } else {
            pedidoDto = pedidoService.getPedidoById(pedido_id);
            if (pedidoDto != null) {
                pedidoCacheService.cachePedido(String.valueOf(pedido_id), pedidoDto);
                return ResponseEntity.ok(pedidoDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @CachePut(value = "pedidos", key = "#pedido_id")
    @PutMapping("/{pedido_id}/checkout")
    public ResponseEntity<String> realizarCheckout(@PathVariable long pedido_id) {
        boolean checkoutRealizado = pedidoCacheService.realizarCheckout(String.valueOf(pedido_id));
        if (checkoutRealizado) {
            return ResponseEntity.ok("Checkout realizado com sucesso para o pedido " + pedido_id);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Outros métodos para atualizar e remover pedidos...

}
