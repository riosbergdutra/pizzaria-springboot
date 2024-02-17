package br.com.usuario.usuario.services.impl;

import br.com.usuario.usuario.dtos.UsuarioDto;
import br.com.usuario.usuario.services.UsuarioCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UsuarioCacheServiceImpl implements UsuarioCacheService {

    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public UsuarioCacheServiceImpl(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = Objects.requireNonNull(dynamoDbClient, "DynamoDB client não pode ser nulo");
    }

    @Override
    public void cacheUsuario(String key, UsuarioDto usuarioDto) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("id", AttributeValue.builder().n(String.valueOf(usuarioDto.getId())).build());
        itemValues.put("email", AttributeValue.builder().s(usuarioDto.getEmail()).build());
        itemValues.put("cache_value", AttributeValue.builder().s(usuarioDto.toJson()).build());
    
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName("usuarios")
                .item(itemValues)
                .build());
    }
    

    @Override
    public UsuarioDto getUsuarioFromCache(String id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().n(id).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName("usuarios")
                .key(key)
                .build();

        try {
            GetItemResponse response = dynamoDbClient.getItem(request);
            Map<String, AttributeValue> item = response.item();

            if (item != null && item.containsKey("cache_value")) {
                AttributeValue cacheValue = item.get("cache_value");
                if (cacheValue != null && cacheValue.s() != null) {
                    return UsuarioDto.fromJson(cacheValue.s());
                }
            }

        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }

        return null;
    }

    @Override
    public void removeUsuarioFromCache(String id) {
        Objects.requireNonNull(id, "ID não pode ser nulo");

        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().n(id).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName("usuarios")
                .key(key)
                .build();

        try {
            dynamoDbClient.deleteItem(request);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao deletar item do cache", e);
        }
    }

    @Override
    public void updateUsuarioInCache(String key, UsuarioDto usuarioDto) {
        Objects.requireNonNull(key, "Chave não pode ser nula");
        Objects.requireNonNull(usuarioDto, "UsuarioDto não pode ser nulo");
    
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("id", AttributeValue.builder().n(String.valueOf(usuarioDto.getId())).build());
        itemValues.put("email", AttributeValue.builder().s(usuarioDto.getEmail()).build());
        itemValues.put("cache_value", AttributeValue.builder().s(usuarioDto.toJson()).build());
    
        DynamoDbClient client = DynamoDbClient.builder().build(); // Crie uma instância válida de DynamoDbClient
        client.putItem(PutItemRequest.builder()
                .tableName("usuarios")
                .item(itemValues)
                .build());
    }    

}
