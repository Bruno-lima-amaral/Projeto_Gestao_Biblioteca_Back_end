package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Funcionario;
import com.impacta.biblioteca.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    // ─── GET — Listar todos os funcionários ──────────────────────

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    // ─── GET — Buscar funcionário por ID ─────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        return funcionarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── POST — Criar novo funcionário ───────────────────────────

    @PostMapping
    public ResponseEntity<Funcionario> criar(@RequestBody Funcionario funcionario) {
        Funcionario funcionarioSalvo = funcionarioService.criar(funcionario);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioSalvo);
    }

    // ─── PUT — Editar funcionário por ID ─────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> editar(@PathVariable Long id, @RequestBody Funcionario funcionarioAtualizado) {
        return funcionarioService.editar(id, funcionarioAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── DELETE — Deletar funcionário por ID ─────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (funcionarioService.deletar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
