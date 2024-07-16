package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosTopico(@NotBlank String titulo,
                          @NotBlank String mensagem,
                          @NotNull Long usuarioId,
                          @NotNull Long cursoId) {
}
