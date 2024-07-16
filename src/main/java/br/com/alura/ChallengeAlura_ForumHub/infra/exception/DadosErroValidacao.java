package br.com.alura.ChallengeAlura_ForumHub.infra.exception;

import org.springframework.validation.FieldError;

public record DadosErroValidacao(String campo,
                                 String mensagem) {

    public DadosErroValidacao(FieldError e) {
        this(e.getField(), e.getDefaultMessage());
    }

}
