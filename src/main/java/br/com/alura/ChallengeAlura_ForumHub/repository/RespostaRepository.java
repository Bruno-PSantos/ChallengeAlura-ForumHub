package br.com.alura.ChallengeAlura_ForumHub.repository;

import br.com.alura.ChallengeAlura_ForumHub.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    List<Resposta> findAllByUsuarioId(Long id);

    List<Resposta> findByTopicoId(Long id);

}
