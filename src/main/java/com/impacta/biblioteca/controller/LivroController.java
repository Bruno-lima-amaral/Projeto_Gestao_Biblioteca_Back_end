package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/livros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroService livroService;

    // ─── GET — Listar todos os livros ────────────────────────────

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    // ─── GET — Buscar livro por ID ───────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── POST — Criar novo livro ─────────────────────────────────

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        try {
            Livro livroSalvo = livroService.criar(body);
            return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── PUT — Editar livro por ID ───────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return livroService.editar(id, body)
                .map(livro -> ResponseEntity.ok((Object) livro))
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── DELETE — Deletar livro por ID ───────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (livroService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
