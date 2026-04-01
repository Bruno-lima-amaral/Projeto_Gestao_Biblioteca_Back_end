package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Cliente;
import com.impacta.biblioteca.model.Emprestimo;
import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.ClienteRepository;
import com.impacta.biblioteca.repository.EmprestimoRepository;
import com.impacta.biblioteca.repository.LivroRepository;
import com.impacta.biblioteca.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emprestimos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final ClienteRepository clienteRepository;
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
    public ResponseEntity<?> criar(@RequestBody Map<String, Long> body) {
        Long livroId = body.get("livroId");
        Long clienteId = body.get("clienteId");

        if (livroId == null || clienteId == null) {
            return ResponseEntity.badRequest()
                    .body("livroId e clienteId são obrigatórios.");
        }

        // Busca o livro
        Livro livro = livroRepository.findById(livroId).orElse(null);
        if (livro == null) {
            return ResponseEntity.badRequest().body("Livro não encontrado.");
        }

        // Verifica se já está emprestado
        if (!livro.getDisponivel()) {
            return ResponseEntity.badRequest().body("Livro já está emprestado.");
        }

        // Busca o cliente
        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Cliente não encontrado.");
        }

        // Marca livro como indisponível
        livro.setDisponivel(false);
        livroRepository.save(livro);

        // Cria o empréstimo
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLivro(livro);
        emprestimo.setCliente(cliente);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setPrazoDias(7);
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(7));
        emprestimo.setDataDevolucao(null);
        emprestimo.setRenovacoesRealizadas(0);
        emprestimo.setStatus("ATIVO");

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
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
