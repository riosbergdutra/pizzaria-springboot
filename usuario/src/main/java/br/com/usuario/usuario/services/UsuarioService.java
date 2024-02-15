package br.com.usuario.usuario.services;

import br.com.usuario.usuario.dtos.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto createUserService(UsuarioDto usuarioDto);

    UsuarioDto findUserByIdService(Long id);

    List<UsuarioDto> findAllUsersService ();

    UsuarioDto updateUserService(Long id, UsuarioDto usuarioDto);

    void removeUserService(Long id);
}
