package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Beneficiador;
import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.BeneficiadorRepository;
import com.impacta.biblioteca.repository.LivroRepository;
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

    private final LivroRepository livroRepository;
    private final BeneficiadorRepository beneficiadorRepository;

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
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        String titulo = (String) body.get("titulo");
        String autor = (String) body.get("autor");
        Integer ano = body.get("ano") != null ? ((Number) body.get("ano")).intValue() : null;

        if (titulo == null || autor == null || ano == null) {
            return ResponseEntity.badRequest().body("titulo, autor e ano são obrigatórios.");
        }

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAno(ano);
        livro.setDisponivel(true);

        // Associar beneficiador se fornecido
        if (body.get("beneficiadorId") != null) {
            Long beneficiadorId = ((Number) body.get("beneficiadorId")).longValue();
            Beneficiador beneficiador = beneficiadorRepository.findById(beneficiadorId).orElse(null);
            if (beneficiador == null) {
                return ResponseEntity.badRequest().body("Beneficiador não encontrado.");
            }
            livro.setBeneficiador(beneficiador);
        }

        Livro livroSalvo = livroRepository.save(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    // ─── PUT — Editar livro por ID ───────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return livroRepository.findById(id)
                .map(livroExistente -> {
                    if (body.get("titulo") != null) livroExistente.setTitulo((String) body.get("titulo"));
                    if (body.get("autor") != null) livroExistente.setAutor((String) body.get("autor"));
                    if (body.get("ano") != null) livroExistente.setAno(((Number) body.get("ano")).intValue());
                    if (body.containsKey("disponivel") && body.get("disponivel") != null) {
                        livroExistente.setDisponivel((Boolean) body.get("disponivel"));
                    }

                    // Atualizar beneficiador
                    if (body.containsKey("beneficiadorId")) {
                        Object benefId = body.get("beneficiadorId");
                        if (benefId != null) {
                            Long bId = ((Number) benefId).longValue();
                            beneficiadorRepository.findById(bId).ifPresent(livroExistente::setBeneficiador);
                        } else {
                            livroExistente.setBeneficiador(null);
                        }
                    }

                    Livro livroSalvo = livroRepository.save(livroExistente);
                    return ResponseEntity.ok((Object) livroSalvo);
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
