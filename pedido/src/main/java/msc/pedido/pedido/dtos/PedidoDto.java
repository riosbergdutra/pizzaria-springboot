package msc.pedido.pedido.dtos;

import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import msc.pedido.pedido.enums.StatusPedido;
import msc.pedido.pedido.model.Pedidos;

public record PedidoDto(
     long pedido_id,
     long user_id,
     long product_id,
     int quantidade,
     String endereco_entrega,
     String numero_telefone_entrega,
     double frete,
     double total_pedido,
     Timestamp data_pedido,
     StatusPedido status_pedido
) {
    public PedidoDto(Pedidos novoPedido) {
        this(
            novoPedido.getPedido_id(),
            novoPedido.getUser_id(),
            novoPedido.getProduct_id(),
            novoPedido.getQuantidade(),
            novoPedido.getEndereco_entrega(),
            novoPedido.getNumero_telefone_entrega(),
            novoPedido.getFrete(),
            novoPedido.getTotal_pedido(),
            novoPedido.getData_pedido(),
            novoPedido.getStatus_pedido()
        );
    }
    public static PedidoDto fromJson(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("A string JSON fornecida está vazia ou nula");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, PedidoDto.class);
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