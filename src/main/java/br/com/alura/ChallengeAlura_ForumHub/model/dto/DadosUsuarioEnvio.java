package br.com.alura.ChallengeAlura_ForumHub.model.dto;

import br.com.alura.ChallengeAlura_ForumHub.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosUsuarioEnvio {

    private Long id;
    private String nome;
    private String email;

    public DadosUsuarioEnvio(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }
}
