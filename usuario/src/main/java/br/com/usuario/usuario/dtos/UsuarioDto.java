package br.com.usuario.usuario.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.usuario.usuario.models.Usuario;

public record UsuarioDto(
   Long id,
   String nome_completo,
   String email,
   String senha,
   String endereco,
   String numero_telefone
) {
   public UsuarioDto(Usuario novoUsuario) {
      this(
         novoUsuario.getId(),
         novoUsuario.getNome_completo(),
         novoUsuario.getEmail(),
         novoUsuario.getSenha(),
         novoUsuario.getEndereco(),
         novoUsuario.getNumero_telefone()
      );
   }

   public Long getId() {
      return id;
   }

   public String getEmail() {
      return email;
   }

   
   public static UsuarioDto fromJson(String json) {
      if (json == null || json.isEmpty()) {
          throw new IllegalArgumentException("A string JSON fornecida está vazia ou nula");
      }
      try {
          ObjectMapper objectMapper = new ObjectMapper();
          return objectMapper.readValue(json, UsuarioDto.class);
      } catch (JsonProcessingException e) {
          throw new IllegalArgumentException("Erro ao converter JSON para UsuarioDto", e);
      }
  }

public String toJson() {
   
   try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(this);
  } catch (JsonProcessingException e) {
      throw new RuntimeException("Erro ao converter UsuarioDto para JSON", e);
  }
}
   
}
