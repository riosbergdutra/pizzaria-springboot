package br.com.usuario.usuario.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

import br.com.usuario.usuario.dtos.UsuarioDto;
import br.com.usuario.usuario.services.UsuarioCacheService;

@Service
public class UsuarioCacheServiceimpl implements UsuarioCacheService {

    private final DynamoDbClient dynamoDbClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public UsuarioCacheServiceimpl(DynamoDbClient dynamoDbClient, ObjectMapper objectMapper) {
        this.dynamoDbClient = dynamoDbClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void cacheUsuario(String key, UsuarioDto usuarioDto) {
        try {
            String usuarioDtoJson = objectMapper.writeValueAsString(usuarioDto);

            Map<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("id", AttributeValue.builder().n(String.valueOf(usuarioDto.getId())).build());
            itemValues.put("email", AttributeValue.builder().s(usuarioDto.getEmail()).build());
            itemValues.put("cache_value", AttributeValue.builder().s(usuarioDtoJson).build());

            dynamoDbClient.putItem(PutItemRequest.builder()
                    .tableName("usuarios")
                    .item(itemValues)
                    .build());

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter UsuarioDto para JSON", e);
        }
    }

    @Override
    public UsuarioDto getUsuarioFromCache(String id) {
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
                    return objectMapper.readValue(cacheValue.s(), UsuarioDto.class);
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para UsuarioDto", e);
        } catch (DynamoDbException e) {
            throw new RuntimeException("Erro ao acessar DynamoDB", e);
        }

        return null;
    }
}
