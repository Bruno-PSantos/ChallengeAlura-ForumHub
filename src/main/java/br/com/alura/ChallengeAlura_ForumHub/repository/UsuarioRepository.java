package br.com.alura.ChallengeAlura_ForumHub.repository;

import br.com.alura.ChallengeAlura_ForumHub.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByNome(String nome);

    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

}
