package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Beneficiador;
import com.impacta.biblioteca.repository.BeneficiadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiadores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BeneficiadorController {

    private final BeneficiadorRepository beneficiadorRepository;

    // ─── GET — Listar todos os beneficiadores ────────────────────

    @GetMapping
    public ResponseEntity<List<Beneficiador>> listarTodos() {
        List<Beneficiador> beneficiadores = beneficiadorRepository.findAll();
        return ResponseEntity.ok(beneficiadores);
    }

    // ─── GET — Buscar beneficiador por ID ────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiador> buscarPorId(@PathVariable Long id) {
        return beneficiadorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── POST — Cadastrar novo beneficiador ─────────────────────

    @PostMapping
    public ResponseEntity<Beneficiador> criar(@RequestBody Beneficiador beneficiador) {
        beneficiador.setId(null);
        Beneficiador salvo = beneficiadorRepository.save(beneficiador);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
