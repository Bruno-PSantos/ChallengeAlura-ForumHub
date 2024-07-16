package br.com.alura.ChallengeAlura_ForumHub.infra.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErro {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(exception.getFieldErrors().stream().map(DadosErroValidacao::new)
                        .toList());
    }

}
