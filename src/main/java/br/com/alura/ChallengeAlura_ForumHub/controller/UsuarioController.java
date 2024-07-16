package br.com.alura.ChallengeAlura_ForumHub.controller;

import br.com.alura.ChallengeAlura_ForumHub.model.Usuario;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosUsuario;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosUsuarioEnvio;
import br.com.alura.ChallengeAlura_ForumHub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosUsuario dadosUsuario, UriComponentsBuilder uriComponentsBuilder) {
        Usuario usuario = new Usuario(null, dadosUsuario.nome(), dadosUsuario.email(), dadosUsuario.senha(), true);

        Optional<Usuario> emailCadastrado = usuarioRepository.findByEmail(usuario.getEmail());
        if (emailCadastrado.isPresent()) {
            return ResponseEntity.badRequest().body("Email já em uso!");
        }

        Optional<Object> nomeCadastrado = Optional.ofNullable(usuarioRepository.findByNome(usuario.getNome()));
        if (nomeCadastrado.isPresent()) {
            return ResponseEntity.badRequest().body("Nome já em uso!");
        }

        usuarioRepository.save(usuario);

        DadosUsuarioEnvio dadosUsuarioEnvio = new DadosUsuarioEnvio(usuario);
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(dadosUsuarioEnvio.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosUsuarioEnvio);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscar(@PathVariable Long id) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isPresent()) {
            return ResponseEntity.badRequest().body("Não existe usuário com esse id!");
        }

        DadosUsuarioEnvio dadosUsuarioEnvio = new DadosUsuarioEnvio(usuarioExiste.get());

        return ResponseEntity.ok(dadosUsuarioEnvio);
    }

    @GetMapping
    public ResponseEntity listar(@PageableDefault(size = 10, sort = "nome") Pageable paginacao) {
        Optional<Page<Usuario>> usuariosExistem = Optional.ofNullable(usuarioRepository.findAllByAtivoTrue(paginacao));
        if (usuariosExistem.isEmpty()) {
            return ResponseEntity.badRequest().body("Não existem usuários cadastrados no sistema ainda!");
        }

        Page<Usuario> usuarios = usuariosExistem.get();
        Page<DadosUsuarioEnvio> dadosUsuarioEnvios = usuarios.map(DadosUsuarioEnvio::new);

        return ResponseEntity.ok(dadosUsuarioEnvios);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosUsuario dadosUsuario) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }

        var usuario = usuarioExiste.get();
        usuario.setNome(dadosUsuario.nome());
        usuario.setEmail(dadosUsuario.nome());
        usuario.setSenha(dadosUsuario.senha());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(new DadosUsuarioEnvio(usuario));
    }

    @PutMapping("/{id}/ativar")
    @Transactional
    public ResponseEntity ativar(@PathVariable Long id) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }

        Usuario usuario = usuarioExiste.get();
        if (usuario.isAtivo()) {
            return ResponseEntity.badRequest().body("Usuário já está ativo!");
        }

        usuario.setAtivo(true);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().body("Usuário foi ativado!");
    }

    @PutMapping("/{id}/desativar")
    @Transactional
    public ResponseEntity desativar(@PathVariable Long id) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }

        Usuario usuario = usuarioExiste.get();
        if (!usuario.isAtivo()) {
            return ResponseEntity.badRequest().body("Usuário já está inativado!");
        }

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);

        return ResponseEntity.ok().body("Usuário foi desativado!");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }

        usuarioRepository.deleteById(id);

        return ResponseEntity.ok().body("Usuário excluído!");
    }
}
