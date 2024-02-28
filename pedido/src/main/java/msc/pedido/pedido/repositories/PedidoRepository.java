package msc.pedido.pedido.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import msc.pedido.pedido.model.Pedidos;

public interface PedidoRepository extends JpaRepository<Pedidos, Long> {
    
}
