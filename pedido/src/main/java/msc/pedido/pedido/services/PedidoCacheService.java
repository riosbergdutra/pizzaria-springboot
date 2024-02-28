package msc.pedido.pedido.services;

import msc.pedido.pedido.dtos.PedidoDto;

public interface PedidoCacheService {
    void cachePedido(String key, PedidoDto pedidoDto);
    PedidoDto getPedidoFromCache(String pedido_id);
    boolean realizarCheckout(String pedido_id);
}
