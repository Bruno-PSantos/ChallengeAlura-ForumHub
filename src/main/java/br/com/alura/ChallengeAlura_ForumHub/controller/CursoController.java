package br.com.alura.ChallengeAlura_ForumHub.controller;

import br.com.alura.ChallengeAlura_ForumHub.model.Curso;
import br.com.alura.ChallengeAlura_ForumHub.model.dto.DadosCurso;
import br.com.alura.ChallengeAlura_ForumHub.repository.CursoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity cadastar(@RequestBody @Valid DadosCurso dadosCurso, UriComponentsBuilder uriComponentsBuilder) {
        var curso = new Curso(dadosCurso);

        if (cursoRepository.findByNome(curso.getNome()).isPresent()) {
            return ResponseEntity.badRequest().body("Já existe curso com o nome informado!");
        }
        cursoRepository.save(curso);

        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(curso);
    }

    @GetMapping
    public ResponseEntity<Page<Curso>> listar(@PageableDefault(size = 10, sort = "titulo")Pageable paginacao) {
        Optional<Page<Curso>> cursosExistem = Optional.ofNullable(cursoRepository.findAll(paginacao));

        if (cursosExistem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Page<Curso> cursos = cursosExistem.get();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscar(@PathVariable Long id) {
        Optional<Curso> cursoExiste = cursoRepository.findById(id);

        if (cursoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Não existe curso com o id informado!");
        }

        var curso = new Curso(cursoExiste.get());
        return ResponseEntity.ok(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity atualizar(@RequestBody @Valid DadosCurso dadosCurso, @PathVariable Long id) {
        Optional<Curso> cursoEncontrado = cursoRepository.findById(id);

        if (cursoEncontrado.isEmpty()) {
            return ResponseEntity.badRequest().body("Não existe curso com o id informado!");
        }

        Curso curso = new Curso(cursoEncontrado.get());
        return ResponseEntity.ok(cursoRepository.save(curso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity excluir(@PathVariable Long id) {
        Optional<Curso> cursoExiste = cursoRepository.findById(id);

        if (cursoExiste.isEmpty()) {
            return ResponseEntity.badRequest().body("Não existe curos com o id informado!");
        }

        cursoRepository.deleteById(id);

        return ResponseEntity.ok().body("Curso excluído!");
    }
}
