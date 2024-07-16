package br.com.alura.ChallengeAlura_ForumHub.controller;

import br.com.alura.ChallengeAlura_ForumHub.model.*;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosResposta;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosTopico;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosTopicoEnvio;
import br.com.alura.ChallengeAlura_ForumHub.repository.CursoRepository;
import br.com.alura.ChallengeAlura_ForumHub.repository.RespostaRepository;
import br.com.alura.ChallengeAlura_ForumHub.repository.TopicoRepository;
import br.com.alura.ChallengeAlura_ForumHub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespostaRepository respostaRepository;


    @PostMapping
    @Transactional
    private ResponseEntity criar(@RequestBody @Valid DadosTopico dadosTopico, UriComponentsBuilder uriComponentsBuilder) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(dadosTopico.usuarioId());
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }
        Usuario usuario = usuarioExiste.get();

        Optional<Curso> cursoExiste = cursoRepository.findById(dadosTopico.cursoId());
        if (cursoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("O curos do tópico não foi encontrado!");
        }
        Curso curso = cursoExiste.get();

        Optional<Topico> nomeTopicoExiste = Optional.ofNullable(topicoRepository.findByTitulo(dadosTopico.titulo()));
        if (nomeTopicoExiste.isPresent()) {
            return ResponseEntity.badRequest().body("Já existe tópico com esse título!");
        }

        Topico topico = new Topico();
        topico.setMensagem(dadosTopico.mensagem());
        topico.setTitulo(dadosTopico.titulo());
        topico.setUsuario(usuario);
        topico.setCurso(curso);
        topico.setStatusTopico(StatusTopico.NAO_RESPONDIDO);
        topicoRepository.save(topico);

        DadosTopicoEnvio dadosTopicoEnvio = new DadosTopicoEnvio(true, topico);
        var uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(dadosTopicoEnvio);
    }

    @PostMapping("/{id}")
    public ResponseEntity responder(@PathVariable Long id, @RequestBody @Valid DadosResposta dadosResposta) {
        Optional<Topico> topicoExiste = topicoRepository.findById(id);
        if (topicoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado!");
        }
        Topico topico = topicoExiste.get();

        Optional<Usuario> usuarioExiste = usuarioRepository.findById(dadosResposta.usuarioId());
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Dono da resposta não encontrado!");
        }
        Usuario usuario = usuarioExiste.get();

        if (!usuario.isAtivo()) {
            return ResponseEntity.badRequest().body("Usuário não está ativo!");
        }

        if (topico.getStatusTopico().equals(StatusTopico.FECHADO)) {
            return ResponseEntity.badRequest().body("Tópico fechado! Não é possível enviar respostas.");
        }

        if (topico.getStatusTopico().equals(StatusTopico.NAO_RESPONDIDO)) {
            topico.setStatusTopico(StatusTopico.NAO_SOLUCIONADO);
            topicoRepository.save(topico);
        }

        Resposta resposta = new Resposta();
        resposta.setUsuario(usuario);
        resposta.setTopico(topico);
        resposta.setMensagem(dadosResposta.mensagem());
        respostaRepository.save(resposta);

        return ResponseEntity.ok().body(new DadosTopicoEnvio(true, topico));
    }

    @GetMapping("/{id}")
    public ResponseEntity buscar(@PathVariable Long id) {
        Optional<Topico> topicoExiste = topicoRepository.findById(id);
        if (topicoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado!");
        }

        Topico topico = topicoExiste.get();
        DadosTopicoEnvio dadosTopicoEnvio = new DadosTopicoEnvio(true, topico);

        return ResponseEntity.ok(dadosTopicoEnvio);
    }

    @GetMapping
    public ResponseEntity<Page<DadosTopicoEnvio>> listar(@PageableDefault(size = 10, sort = "dataCriacao")Pageable paginacao) {
        Page<Topico> topicos = topicoRepository.findAll(paginacao);
        List<DadosTopicoEnvio> listaDadosTopicoEnvios = DadosTopicoEnvio.convercao(topicos.getContent());

        Page<DadosTopicoEnvio> dadosTopicoEnvios = new PageImpl<>(listaDadosTopicoEnvios, paginacao, topicos.getTotalElements());

        return ResponseEntity.ok(dadosTopicoEnvios);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosTopico dadosTopico) {
        Optional<Topico> topicoExiste = topicoRepository.findById(id);
        if (topicoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado!");
        }
        Topico topico = topicoExiste.get();

        if (topico.getStatusTopico().equals(StatusTopico.FECHADO)) {
            return ResponseEntity.badRequest().body("Tópico fechado! Não é possível atualizar o tópico.");
        }

        Optional<Curso> cursoExiste = cursoRepository.findById(topico.getId());
        if (cursoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Curso não encontrado!");
        }
        Curso curso = cursoExiste.get();

        Optional<Usuario> usuarioExiste = usuarioRepository.findById(topico.getUsuario().getId());
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Dono do tópico não encontrado!");
        }
        Usuario usuario = usuarioExiste.get();

        topico.setTitulo(dadosTopico.titulo());
        topico.setCurso(curso);
        topico.setUsuario(usuario);
        topico.setMensagem(dadosTopico.mensagem());
        topicoRepository.save(topico);

        DadosTopicoEnvio dadosTopicoEnvio = new DadosTopicoEnvio(true, topico);

        return ResponseEntity.ok(dadosTopicoEnvio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Topico> topicoExiste = topicoRepository.findById(id);
        if (topicoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado!");
        }

        topicoRepository.deleteById(id);

        return ResponseEntity.ok().body("Tópico excluído!");
    }
}
