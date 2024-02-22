package br.com.usuario.usuario.services.impl;

import br.com.usuario.usuario.dtos.UsuarioDto;
import br.com.usuario.usuario.models.Usuario;
import br.com.usuario.usuario.repositories.UsuarioRepository;
import br.com.usuario.usuario.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.awspring.cloud.sqs.operations.SqsTemplate;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SqsTemplate sqsTemplate; // Injeção do SqsTemplate

    @Override
    public UsuarioDto createUserService(UsuarioDto usuarioDto) {
        Usuario usuarioJaExiste = usuarioRepository.findByEmail(usuarioDto.email());
        if (usuarioJaExiste != null) {
            throw new RuntimeException("Usuário já existe!");
        }
        String senhaHash = passwordEncoder.encode(usuarioDto.senha());
        Usuario usuario = new Usuario(null, usuarioDto.nome_completo(), usuarioDto.email(), senhaHash,
                usuarioDto.endereco(), usuarioDto.nome_completo(), null, null);
        Usuario novoUsuario = usuarioRepository.save(usuario);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Novo usuário cadastrado: " + usuarioDto.getEmail();
        sqsTemplate.send(queueUrl, messageBody);

        return new UsuarioDto(novoUsuario);
    }

    @Override
    public UsuarioDto findUserByIdService(Long id) {
        @SuppressWarnings("null")
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Usuário encontrado pelo ID: " + id;
            sqsTemplate.send(queueUrl, messageBody);
        }
        return optionalUsuario.map(UsuarioDto::new).orElse(null);
    }

    @Override
    public List<UsuarioDto> findAllUsersService() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Consultando todos os usuários";
        sqsTemplate.send(queueUrl, messageBody);
        return usuarios.stream().map(UsuarioDto::new).collect(Collectors.toList());
    }

    @Override
    public UsuarioDto updateUserService(Long id, UsuarioDto usuarioDto) {
        @SuppressWarnings("null")
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNome_completo(usuarioDto.getNome_completo());
            usuario.setEmail(usuarioDto.getEmail());
            usuario.setEndereco(usuarioDto.getEndereco());
            usuario.setNumero_telefone(usuarioDto.getNumero_telefone());

            if (!usuarioDto.getSenha().equals(usuario.getSenha())) {
                String senhaHash = passwordEncoder.encode(usuarioDto.getSenha());
                usuario.setSenha(senhaHash);
            }

            usuarioRepository.save(usuario);

            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Usuário atualizado pelo ID: " + id;
            sqsTemplate.send(queueUrl, messageBody);

            return new UsuarioDto(usuario);
        } else {
            return null;
        }
    }

    @SuppressWarnings("null")
    @Override
    public void removeUserService(Long id) {
        usuarioRepository.deleteById(id);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Usuário removido pelo ID: " + id;
        sqsTemplate.send(queueUrl, messageBody);
    }
}