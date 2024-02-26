package br.com.usuario.usuario.services;

import br.com.usuario.usuario.dtos.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto createUserService(UsuarioDto usuarioDto);

    UsuarioDto findUserByIdService(Long user_id);

    List<UsuarioDto> findAllUsersService ();

    UsuarioDto updateUserService(Long user_id, UsuarioDto usuarioDto);

    void removeUserService(Long user_id);
}
