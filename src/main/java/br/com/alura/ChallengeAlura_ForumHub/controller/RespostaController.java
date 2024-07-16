package br.com.alura.ChallengeAlura_ForumHub.controller;

import br.com.alura.ChallengeAlura_ForumHub.model.Resposta;
import br.com.alura.ChallengeAlura_ForumHub.model.StatusTopico;
import br.com.alura.ChallengeAlura_ForumHub.model.Topico;
import br.com.alura.ChallengeAlura_ForumHub.model.Usuario;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosResposta;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosRespostaEnvio;
import br.com.alura.ChallengeAlura_ForumHub.repository.RespostaRepository;
import br.com.alura.ChallengeAlura_ForumHub.repository.TopicoRepository;
import br.com.alura.ChallengeAlura_ForumHub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity responder(@RequestBody @Valid DadosResposta dadosResposta) {
        Optional<Topico> topicoExiste = topicoRepository.findById(dadosResposta.topicoId());
        if (topicoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Tópico não encontrado com o id: " + dadosResposta.topicoId());
        }

        var topico = topicoExiste.get();

        Optional<Usuario> usuarioExiste = usuarioRepository.findById(dadosResposta.usuarioId());
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado!");
        }

        var usuario = usuarioExiste.get();

        if (!usuario.isAtivo()) {
            return ResponseEntity.badRequest().body("Usuário não está ativo no sistema!");
        }

        if (topico.getStatusTopico().equals(StatusTopico.FECHADO)) {
            return ResponseEntity.badRequest().body("Tópico fechado! Não é possível enviar respostas.");
        }

        if (topico.getStatusTopico().equals(StatusTopico.NAO_RESPONDIDO)) {
            topico.setStatusTopico(StatusTopico.NAO_SOLUCIONADO);
            topicoRepository.save(topico);
        }

        Resposta resposta = new Resposta();
        resposta.setMensagem(dadosResposta.mensagem());
        resposta.setUsuario(usuario);
        resposta.setTopico(topico);
        respostaRepository.save(resposta);

        return ResponseEntity.ok().body("Resposta enviada!");
    }

    @GetMapping("/usuario/{id}")
    @Transactional
    public ResponseEntity<List<DadosRespostaEnvio>> listarRespostasPorUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<DadosRespostaEnvio> respostaEnviadasUsuario = DadosRespostaEnvio.convercao(respostaRepository.findAllByUsuarioId(usuarioExiste.get().getId()));

        return ResponseEntity.ok().body(respostaEnviadasUsuario);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosResposta dadosResposta) {
        Optional<Resposta> respostaExiste = respostaRepository.findById(id);
        if (respostaExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Resposta não encontrada!");
        }

        var resposta = respostaExiste.get();
        resposta.setMensagem(dadosResposta.mensagem());

        respostaRepository.save(resposta);

        return ResponseEntity.ok().body("Resposta atualizada!");
    }

    @PutMapping("{id}/solucao")
    @Transactional
    public ResponseEntity respostaSolucao(@PathVariable Long id) {
        Optional<Resposta> respostaExiste = respostaRepository.findById(id);
        if (respostaExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Resposta não encontrada!");
        }

        var resposta = respostaExiste.get();
        Topico topico = resposta.getTopico();
        topico.setStatusTopico(StatusTopico.SOLUCIONADO);
        topicoRepository.save(topico);

        resposta.setSolucao(true);
        respostaRepository.save(resposta);

        return ResponseEntity.ok().body("Resposta marcada como solução para o tópico!");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Resposta> respostaExiste = respostaRepository.findById(id);
        if (respostaExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Resposta não encontrada!");
        }

        respostaRepository.deleteById(id);

        return ResponseEntity.ok().body("Resposta excluída!");
    }
}
