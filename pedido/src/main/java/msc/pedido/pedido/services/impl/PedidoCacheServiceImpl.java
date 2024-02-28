package msc.pedido.pedido.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import msc.pedido.pedido.dtos.PedidoDto;
import msc.pedido.pedido.enums.StatusPedido;
import msc.pedido.pedido.model.Pedidos;
import msc.pedido.pedido.services.PedidoCacheService;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class PedidoCacheServiceImpl implements PedidoCacheService {

    private final DynamoDbClient dynamoDbClient;



    @Autowired
    public PedidoCacheServiceImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = Objects.requireNonNull(dynamoDbClient, "DynamoDB client não pode ser nulo");
    }

    @Override
    public void cachePedido(String key, PedidoDto pedidoDto) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("pedido_id", AttributeValue.builder().n(String.valueOf(pedidoDto.getPedido_id())).build());
        itemValues.put("cache_value", AttributeValue.builder().s(pedidoDto.toJson()).build());

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName("pedidos")
                .item(itemValues)
                .build());
    }

    @Override
    public PedidoDto getPedidoFromCache(String pedido_id) {
        Objects.requireNonNull(pedido_id, "ID do pedido não pode ser nulo");

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("pedido_id", AttributeValue.builder().n(pedido_id).build());
        GetItemRequest request = GetItemRequest.builder()
                .tableName("pedidos")
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDbClient.getItem(request);
            Map<String, AttributeValue> item = response.item();

            if (item != null && item.containsKey("cache_value")) {
                AttributeValue cacheValue = item.get("cache_value");
                if (cacheValue != null && cacheValue.s() != null) {
                    return PedidoDto.fromJson(cacheValue.s());
                }
            }

        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }

        return null;
    }

    @Override
    public boolean realizarCheckout(String pedido_id) {
        Objects.requireNonNull(pedido_id, "ID do pedido não pode ser nulo");
    
        // Obtém o pedido do cache
        PedidoDto pedidoDto = getPedidoFromCache(pedido_id);
        if (pedidoDto != null) {
            // Atualiza o status do pedido para "concluído"
            Pedidos pedido = new Pedidos();
            pedido.setPedido_id(Long.parseLong(pedido_id));
            pedido.setStatus_pedido(StatusPedido.concluido);
    
            // Atualiza o pedido no cache
            cachePedido(pedido_id, new PedidoDto(pedido));
    
            // Retorna true para indicar que o checkout foi realizado com sucesso
            return true;
        }
    
        // Retorna false se o pedido não foi encontrado no cache
        return false;
    }    
    
}
