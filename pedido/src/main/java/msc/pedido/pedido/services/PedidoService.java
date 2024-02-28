package msc.pedido.pedido.services;

import msc.pedido.pedido.dtos.PedidoDto;

public interface PedidoService {

    PedidoDto criarPedido(PedidoDto pedido_id);

    PedidoDto getPedidoById(long pedido_id);

    boolean realizarCheckout(long pedido_id);
}
