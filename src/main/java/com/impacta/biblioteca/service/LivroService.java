package com.impacta.biblioteca.service;

import com.impacta.biblioteca.model.Beneficiador;
import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.BeneficiadorRepository;
import com.impacta.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final BeneficiadorRepository beneficiadorRepository;

    // ─── Listar todos os livros ──────────────────────────────────

    public List<Livro> listarTodos() {
        return livroRepository.findAll();
    }

    // ─── Buscar livro por ID ─────────────────────────────────────

    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    // ─── Criar novo livro ────────────────────────────────────────

    public Livro criar(Map<String, Object> body) {
        String titulo = (String) body.get("titulo");
        String autor = (String) body.get("autor");
        Integer ano = body.get("ano") != null ? ((Number) body.get("ano")).intValue() : null;

        if (titulo == null || autor == null || ano == null) {
            throw new IllegalArgumentException("titulo, autor e ano são obrigatórios.");
        }

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAutor(autor);
        livro.setAno(ano);
        livro.setDisponivel(true);

        // Novos campos
        if (body.get("genero") != null) livro.setGenero((String) body.get("genero"));
        if (body.get("isbn") != null) livro.setIsbn((String) body.get("isbn"));

        // Associar beneficiador se fornecido
        if (body.get("beneficiadorId") != null) {
            Long beneficiadorId = ((Number) body.get("beneficiadorId")).longValue();
            Beneficiador beneficiador = beneficiadorRepository.findById(beneficiadorId)
                    .orElseThrow(() -> new RuntimeException("Beneficiador não encontrado."));
            livro.setBeneficiador(beneficiador);
        }

        return livroRepository.save(livro);
    }

    // ─── Editar livro por ID ─────────────────────────────────────

    public Optional<Livro> editar(Long id, Map<String, Object> body) {
        return livroRepository.findById(id)
                .map(livroExistente -> {
                    if (body.get("titulo") != null) livroExistente.setTitulo((String) body.get("titulo"));
                    if (body.get("autor") != null) livroExistente.setAutor((String) body.get("autor"));
                    if (body.get("ano") != null) livroExistente.setAno(((Number) body.get("ano")).intValue());
                    if (body.containsKey("disponivel") && body.get("disponivel") != null) {
                        livroExistente.setDisponivel((Boolean) body.get("disponivel"));
                    }

                    // Novos campos
                    if (body.containsKey("genero")) livroExistente.setGenero((String) body.get("genero"));
                    if (body.containsKey("isbn")) livroExistente.setIsbn((String) body.get("isbn"));

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

                    return livroRepository.save(livroExistente);
                });
    }

    // ─── Deletar livro por ID ────────────────────────────────────

    public boolean deletar(Long id) {
        return livroRepository.findById(id)
                .map(livro -> {
                    livroRepository.delete(livro);
                    return true;
                })
                .orElse(false);
    }
}
