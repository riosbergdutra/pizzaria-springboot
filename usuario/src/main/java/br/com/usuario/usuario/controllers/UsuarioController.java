package br.com.usuario.usuario.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.usuario.usuario.dtos.UsuarioDto;
import br.com.usuario.usuario.services.UsuarioCacheService;
import br.com.usuario.usuario.services.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@EnableCaching
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioCacheService usuarioCacheService;

    @PostMapping
    public ResponseEntity<UsuarioDto> createUserController(@RequestBody UsuarioDto usuarioDto) {
        UsuarioDto novoUsuario = usuarioService.createUserService(usuarioDto);
        usuarioCacheService.cacheUsuario(novoUsuario.getId().toString(), novoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }
    @Cacheable("usuarios")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findUserByIdController(@PathVariable Long id) {
        UsuarioDto usuarioDto = usuarioCacheService.getUsuarioFromCache(id.toString());
        if (usuarioDto != null) {
            return ResponseEntity.ok(usuarioDto);
        } else {
            usuarioDto = usuarioService.findUserByIdService(id);
            if (usuarioDto != null) {
                return ResponseEntity.ok(usuarioDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAllUsersController() {
        List<UsuarioDto> usuarios = usuarioService.findAllUsersService();
        return ResponseEntity.ok(usuarios);
    }

     @PutMapping("/{id}")
    @CachePut(value = "usuarios", key = "#id")
    public ResponseEntity<UsuarioDto> updateUserController(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        UsuarioDto usuarioAtualizado = usuarioService.updateUserService(id, usuarioDto);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @CacheEvict(value = "usuarios", key = "#id")    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUserController(@PathVariable Long id) {
        usuarioService.removeUserService(id);
        usuarioCacheService.removeUsuarioFromCache(id.toString()); // Remover do cache
        return ResponseEntity.noContent().build();
    }
    
}