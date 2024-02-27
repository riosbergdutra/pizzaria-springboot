package msc.carrinho.carrinho.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import msc.carrinho.carrinho.dtos.CarrinhoDto;
import msc.carrinho.carrinho.services.CarrinhoCacheService;

@Service
public class CarrinhoCacheServiceImpl implements CarrinhoCacheService {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public CarrinhoCacheServiceImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = Objects.requireNonNull(dynamoDbClient, "DynamoDB client não pode ser nulo");
    }

    @Override
    public void cacheCarrinho(String key, CarrinhoDto carrinhoDto) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("carrinho_id", AttributeValue.builder().n(String.valueOf(carrinhoDto.getCarrinho_id())).build());
        itemValues.put("cache_value", AttributeValue.builder().s(carrinhoDto.toJson()).build());
        putItem("carrinho", itemValues);
    }

    @Override
    public CarrinhoDto getCarrinhoFromCache(String carrinhoId) {
        Objects.requireNonNull(carrinhoId, "ID do carrinho não pode ser nulo");
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("carrinho_id", AttributeValue.builder().n(carrinhoId).build());
        return getItem("carrinho", key);
    }

    @Override
    public void removeCarrinhoFromCache(String carrinhoId) {
        Objects.requireNonNull(carrinhoId, "ID do carrinho não pode ser nulo");
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("carrinho_id", AttributeValue.builder().n(carrinhoId).build());
        deleteItem("carrinho", key);
    }

    @Override
    public void updateCarrinhoInCache(String key, CarrinhoDto carrinhoDto) {
        Objects.requireNonNull(key, "Chave do carrinho não pode ser nula");
        Objects.requireNonNull(carrinhoDto, "DTO do carrinho não pode ser nulo");
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("carrinho_id", AttributeValue.builder().n(String.valueOf(carrinhoDto.getCarrinho_id())).build());
        itemValues.put("cache_value", AttributeValue.builder().s(carrinhoDto.toJson()).build());
        putItem("carrinho", itemValues);
    }

    private void putItem(String tableName, Map<String, AttributeValue> itemValues) {
        try {
            dynamoDbClient.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(itemValues)
                    .build());
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }
    }

    private CarrinhoDto getItem(String tableName, Map<String, AttributeValue> key) {
        try {
            GetItemResponse response = dynamoDbClient.getItem(GetItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build());
            Map<String, AttributeValue> item = response.item();
            if (item != null && item.containsKey("cache_value")) {
                AttributeValue cacheValue = item.get("cache_value");
                if (cacheValue != null && cacheValue.s() != null) {
                    return CarrinhoDto.fromJson(cacheValue.s());
                }
            }
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }
        return null;
    }

    private void deleteItem(String tableName, Map<String, AttributeValue> key) {
        try {
            dynamoDbClient.deleteItem(DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build());
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao deletar item do cache", e);
        }
    }
}
