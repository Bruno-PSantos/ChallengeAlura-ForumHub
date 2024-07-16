package br.com.alura.ChallengeAlura_ForumHub.repository;

import br.com.alura.ChallengeAlura_ForumHub.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Topico findByTitulo(String titulo);

    List<Topico> findAllByUsuarioId(Long id);

}
