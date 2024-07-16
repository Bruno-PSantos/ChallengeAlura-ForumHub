package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosUsuario(@NotBlank String nome,
                           @NotBlank @Email String email,
                           @NotBlank String senha) {
}
