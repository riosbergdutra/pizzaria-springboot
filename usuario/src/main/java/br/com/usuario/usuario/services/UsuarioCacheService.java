package br.com.usuario.usuario.services;

import br.com.usuario.usuario.dtos.UsuarioDto;

public interface UsuarioCacheService {
    void cacheUsuario(String key, UsuarioDto usuarioDto);
    UsuarioDto getUsuarioFromCache(String key);
    void removeUsuarioFromCache(String key);
    void updateUsuarioInCache(String key, UsuarioDto usuarioDto);
}
