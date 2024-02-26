package br.com.commerce.ecommerce.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.commerce.ecommerce.dtos.ProdutoDto;
import br.com.commerce.ecommerce.services.ProdutoCacheService;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ProdutoCacheServiceImpl implements ProdutoCacheService {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public ProdutoCacheServiceImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = Objects.requireNonNull(dynamoDbClient, "DynamoDB client não pode ser nulo");
    }

    @Override
    public void cacheProduto(String key, ProdutoDto produtoDto) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("product_id", AttributeValue.builder().n(String.valueOf(produtoDto.getProduct_id())).build());
        itemValues.put("nome", AttributeValue.builder().s(produtoDto.getNome()).build());
        itemValues.put("cache_value", AttributeValue.builder().s(produtoDto.toJson()).build());
    
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName("produtos")
                .item(itemValues)
                .build());
    }
    

    @Override
    public ProdutoDto getProdutoFromCache(String product_id) {
        Objects.requireNonNull(product_id, "ID não pode ser nulo");

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("product_id", AttributeValue.builder().n(product_id).build());
        GetItemRequest request = GetItemRequest.builder()
                .tableName("produtos")
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDbClient.getItem(request);
            Map<String, AttributeValue> item = response.item();

            if (item != null && item.containsKey("cache_value")) {
                AttributeValue cacheValue = item.get("cache_value");
                if (cacheValue != null && cacheValue.s() != null) {
                    return ProdutoDto.fromJson(cacheValue.s());
                }
            }

        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }

        return null;
    }

    @Override
    public void removeProdutoFromCache(String product_id) {
        Objects.requireNonNull(product_id, "ID não pode ser nulo");

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("product_id", AttributeValue.builder().n(product_id).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName("produtos")
                .key(key)
                .build();

        try {
            dynamoDbClient.deleteItem(request);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao deletar item do cache", e);
        }
    }

    @Override
    public void updateProdutoInCache(String key, ProdutoDto produtoDto) {
        Objects.requireNonNull(key, "Chave não pode ser nula");
        Objects.requireNonNull(produtoDto, "ProdutoDto não pode ser nulo");
    
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("product_id", AttributeValue.builder().n(String.valueOf(produtoDto.getProduct_id())).build());
        itemValues.put("nome", AttributeValue.builder().s(produtoDto.getNome()).build());
        itemValues.put("cache_value", AttributeValue.builder().s(produtoDto.toJson()).build());
    
        dynamoDbClient.putItem(PutItemRequest.builder() 
                .tableName("produtos")
                .item(itemValues)
                .build());
    }
}
