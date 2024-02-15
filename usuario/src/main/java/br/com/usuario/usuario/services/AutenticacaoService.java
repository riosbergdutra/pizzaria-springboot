package br.com.usuario.usuario.services;



import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.usuario.usuario.dtos.AuthDto;
public interface AutenticacaoService extends UserDetailsService {

    public String obterToken(AuthDto authDto);
    
    public String validaTokenJWT(String token);
} 