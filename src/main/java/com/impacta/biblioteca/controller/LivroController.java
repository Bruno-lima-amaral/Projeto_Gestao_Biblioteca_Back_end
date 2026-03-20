package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LivroController {

    private final LivroRepository livroRepository;

    // ─── GET — Listar todos os livros ────────────────────────────

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> livros = livroRepository.findAll();
        return ResponseEntity.ok(livros);
    }

    // ─── GET — Buscar livro por ID ───────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return livroRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── POST — Criar novo livro ─────────────────────────────────

    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody Livro livro) {
        livro.setId(null); // Garante que será um novo registro
        livro.setDisponivel(true); // Novo livro sempre disponível
        Livro livroSalvo = livroRepository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    // ─── PUT — Editar livro por ID ───────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<Livro> editar(@PathVariable Long id, @RequestBody Livro livroAtualizado) {
        return livroRepository.findById(id)
                .map(livroExistente -> {
                    livroExistente.setTitulo(livroAtualizado.getTitulo());
                    livroExistente.setAutor(livroAtualizado.getAutor());
                    livroExistente.setAno(livroAtualizado.getAno());
                    livroExistente.setDisponivel(livroAtualizado.getDisponivel());
                    Livro livroSalvo = livroRepository.save(livroExistente);
                    return ResponseEntity.ok(livroSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── DELETE — Deletar livro por ID ───────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livroRepository.delete(livro);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
