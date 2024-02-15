package br.com.usuario.usuario.dtos;

import br.com.usuario.usuario.models.Usuario;

public record UsuarioDto(
   String nome_completo,
   String email,
   String senha,
   String endereco,
   String numero_telefone
) {
   public UsuarioDto(Usuario novoUsuario) {
      this(
         novoUsuario.getNome_completo(),
         novoUsuario.getEmail(),
         novoUsuario.getSenha(),
         novoUsuario.getEndereco(),
         novoUsuario.getNumero_telefone()
      );
   }


}
