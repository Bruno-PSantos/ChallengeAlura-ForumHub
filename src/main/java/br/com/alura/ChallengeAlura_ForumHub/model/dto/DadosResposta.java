package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosResposta(@NotBlank String mensagem,
                            @NotNull Long usuarioId,
                            Long topicoId) {
}
