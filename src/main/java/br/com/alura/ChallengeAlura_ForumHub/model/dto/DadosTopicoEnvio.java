package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import br.com.alura.ChallengeAlura_ForumHub.model.Resposta;
import br.com.alura.ChallengeAlura_ForumHub.model.Topico;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DadosTopicoEnvio {

    Long id;
    String titulo;
    String mensagem;
    String status;
    String dataCriacao;
    String nomeUsuario;
    Long idUsuario;
    List<DadosRespostaEnvio> respostas;
    String nomeCurso;

    public DadosTopicoEnvio(Boolean respostas, Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.mensagem = topico.getMensagem();
        this.status = topico.getStatusTopico().toString();
        this.dataCriacao = topico.getDataCriacao().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        this.nomeCurso = topico.getCurso().getNome();
        this.nomeUsuario = topico.getUsuario().getNome();
        this.idUsuario = topico.getUsuario().getId();

        if (respostas){
            this.respostas = adicionarRespostasAoTopico(topico.getRespostas());
        } else {
            this.respostas = null;
        }
    }


    public static List<DadosTopicoEnvio> convercao(List<Topico> topicos) {
        List<DadosTopicoEnvio> dadosTopicoEnvios = new ArrayList<>();
        topicos.forEach(t -> dadosTopicoEnvios.add(new DadosTopicoEnvio(false, t)));

        return dadosTopicoEnvios;
    }

    private List<DadosRespostaEnvio> adicionarRespostasAoTopico(List<Resposta> respostas) {
        List<DadosRespostaEnvio> dadosRespostaEnvios = new ArrayList<>();
        respostas.forEach(r -> dadosRespostaEnvios.add(new DadosRespostaEnvio(false, r)));

        if (dadosRespostaEnvios.isEmpty()) {
            return null;
        }

        return dadosRespostaEnvios;
    }

}
