package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Cliente;
import com.impacta.biblioteca.model.Emprestimo;
import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.ClienteRepository;
import com.impacta.biblioteca.repository.EmprestimoRepository;
import com.impacta.biblioteca.repository.LivroRepository;
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
        emprestimo.setDataDevolucao(null);
        emprestimo.setStatus("ATIVO");

        Emprestimo salvo = emprestimoRepository.save(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // ─── PUT — Devolver empréstimo ──────────────────────────────────

    @PutMapping("/{id}/devolver")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        return emprestimoRepository.findById(id)
                .map(emprestimo -> {
                    // Marca livro como disponível novamente
                    Livro livro = emprestimo.getLivro();
                    livro.setDisponivel(true);
                    livroRepository.save(livro);

                    // Atualiza o empréstimo
                    emprestimo.setDataDevolucao(LocalDate.now());
                    emprestimo.setStatus("DEVOLVIDO");
                    Emprestimo atualizado = emprestimoRepository.save(emprestimo);

                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
