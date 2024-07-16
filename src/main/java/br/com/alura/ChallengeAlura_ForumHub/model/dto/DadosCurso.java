package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosCurso(@NotBlank String nome,
                         @NotBlank String categoria) {
}
