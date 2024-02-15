package br.com.usuario.usuario.dtos;

public record AuthDto(String email,
String senha) {

    public String getEmail() {
        return email;
    }

    

  
    
}
