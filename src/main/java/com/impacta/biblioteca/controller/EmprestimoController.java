package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Emprestimo;
import com.impacta.biblioteca.repository.EmprestimoRepository;
import com.impacta.biblioteca.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final EmprestimoService emprestimoService;

    // ─── GET — Listar todos os empréstimos ──────────────────────────

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();
        return ResponseEntity.ok(emprestimos);
    }

    // ─── POST — Criar novo empréstimo ───────────────────────────────
    // Recebe { "livroId": 1, "clienteId": 2 }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        Number livroIdNum = (Number) body.get("livroId");
        Number clienteIdNum = (Number) body.get("clienteId");

        if (livroIdNum == null || clienteIdNum == null) {
            return ResponseEntity.badRequest()
                    .body("livroId e clienteId são obrigatórios.");
        }

        try {
            Emprestimo salvo = emprestimoService.criarEmprestimo(
                    livroIdNum.longValue(), clienteIdNum.longValue());
            return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── PUT — Renovar empréstimo ───────────────────────────────────

    @PutMapping("/{id}/renovar")
    public ResponseEntity<?> renovar(@PathVariable Long id) {
        try {
            Emprestimo renovado = emprestimoService.renovarEmprestimo(id);
            return ResponseEntity.ok(renovado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ─── PUT — Devolver empréstimo ──────────────────────────────────

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            Emprestimo devolvido = emprestimoService.registrarDevolucao(id);
            return ResponseEntity.ok(devolvido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
