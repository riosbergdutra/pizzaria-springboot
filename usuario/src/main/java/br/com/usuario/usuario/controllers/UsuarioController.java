package br.com.usuario.usuario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.usuario.usuario.dtos.UsuarioDto;
import br.com.usuario.usuario.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioDto> createUserController(@RequestBody UsuarioDto usuarioDto) {
        UsuarioDto novoUsuario = usuarioService.createUserService(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUserByIdController(@PathVariable Long id) {
        UsuarioDto usuarioDto = usuarioService.findUserByIdService(id);
        if (usuarioDto != null) {
            return ResponseEntity.ok(usuarioDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAllUsersController() {
        List<UsuarioDto> usuarios = usuarioService.findAllUsersService();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> updateUserController(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioAtualizado = usuarioService.updateUserService(id, usuarioDto);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUserController(@PathVariable Long id) {
        usuarioService.removeUserService(id);
        return ResponseEntity.noContent().build();
    }
}
