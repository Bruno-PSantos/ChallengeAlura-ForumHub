package br.com.alura.ChallengeAlura_ForumHub.controller;

import br.com.alura.ChallengeAlura_ForumHub.infra.security.DadosAutenticacao;
import br.com.alura.ChallengeAlura_ForumHub.infra.security.DadosTokenJWT;
import br.com.alura.ChallengeAlura_ForumHub.infra.security.TokenService;
import br.com.alura.ChallengeAlura_ForumHub.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity logar(@RequestBody @Valid DadosAutenticacao dadosAutenticacao) {
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(dadosAutenticacao.nome(), dadosAutenticacao.senha());

        var autenticacao = authenticationManager.authenticate(tokenAutenticacao);

        var tokenJWT = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

}
