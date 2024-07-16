package br.com.alura.ChallengeAlura_ForumHub.model;

import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosCurso;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "cursos")
@Entity(name = "Curso")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nome;

    private String categoria;

    public Curso(DadosCurso dadosCurso) {
        this.nome = dadosCurso.nome();
        this.categoria = dadosCurso.categoria();
    }

    public Curso(Curso curso) {
        this.id = curso.id;
        this.nome = curso.nome;
        this.categoria = curso.categoria;
    }
}