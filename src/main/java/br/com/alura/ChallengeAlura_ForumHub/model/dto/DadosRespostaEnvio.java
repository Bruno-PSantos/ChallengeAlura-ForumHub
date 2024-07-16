package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import br.com.alura.ChallengeAlura_ForumHub.model.Resposta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
public class DadosRespostaEnvio {

    @NotBlank
    private Long idResposta;
    @NotBlank
    private String nomeTopico;
    @NotBlank
    private String mensagem;
    @NotBlank
    private String dataCriacao;
    @NotBlank
    private String solucao;
    @NotBlank
    private String nomeUsuario;
    @NotNull
    private Long idUsuario;


    public DadosRespostaEnvio(Boolean respostas, Resposta resposta) {
        this.idResposta = resposta.getId();
        this.mensagem = resposta.getMensagem();
        this.dataCriacao = resposta.getData().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));
        this.nomeUsuario = resposta.getUsuario().getNome();
        this.idUsuario = resposta.getUsuario().getId();

        if(respostas) {
            this.nomeTopico = resposta.getTopico().getTitulo();
        }

        if (!resposta.getSolucao()) {
            this.solucao = "Não";
        } else {
            this.solucao = "Sim";
        }
    }

    public static List<DadosRespostaEnvio> convercao(List<Resposta> respostas) {
        List<DadosRespostaEnvio> respostaEnvios = new ArrayList<>();
        respostas.forEach(r -> respostaEnvios.add(new DadosRespostaEnvio(true , r)));

        if (respostaEnvios.isEmpty()) {
            return null;
        }

        return respostaEnvios;
    }

}
