package br.com.alura.ChallengeAlura_ForumHub.repository;

import br.com.alura.ChallengeAlura_ForumHub.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    Curso findById(long id);

    Optional<Curso> findByNome(String nome);

}
