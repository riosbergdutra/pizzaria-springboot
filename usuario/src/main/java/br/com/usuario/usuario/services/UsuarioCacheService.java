package br.com.usuario.usuario.services;

import br.com.usuario.usuario.dtos.UsuarioDto;

public interface UsuarioCacheService {
    void cacheUsuario(String key, UsuarioDto usuarioDto);
    UsuarioDto getUsuarioFromCache(String user_id);
    void removeUsuarioFromCache(String user_id);
    void updateUsuarioInCache(String key, UsuarioDto usuarioDto);
}
