package com.impacta.biblioteca.service;

import com.impacta.biblioteca.model.Cliente;
import com.impacta.biblioteca.model.Emprestimo;
import com.impacta.biblioteca.model.Livro;
import com.impacta.biblioteca.repository.ClienteRepository;
import com.impacta.biblioteca.repository.EmprestimoRepository;
import com.impacta.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final ClienteRepository clienteRepository;

    /**
     * Cria um novo empréstimo.
     *
     * Todas as operações (buscar livro, buscar cliente, marcar indisponível,
     * salvar empréstimo) rodam na mesma transação/sessão Hibernate.
     */
    @Transactional
    public Emprestimo criarEmprestimo(Long livroId, Long clienteId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado."));

        if (!livro.getDisponivel()) {
            throw new RuntimeException("Livro já está emprestado.");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        // Marca livro como indisponível (mesma sessão)
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

        return emprestimoRepository.save(emprestimo);
    }

    /**
     * Renova um empréstimo existente.
     *
     * Regra 1: O cliente pode renovar o mesmo livro no máximo 2 vezes.
     * Regra 2: Não é permitida a renovação se o cliente possuir devoluções em atraso.
     * Regra 3: Se aprovado, adiciona 7 dias à dataPrevistaDevolucao e incrementa renovacoesRealizadas.
     */
    @Transactional
    public Emprestimo renovarEmprestimo(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com id: " + id));

        // Regra 1: Máximo de 2 renovações
        if (emprestimo.getRenovacoesRealizadas() >= 2) {
            throw new RuntimeException(
                    "Limite de renovações atingido. O cliente já renovou este empréstimo " +
                    emprestimo.getRenovacoesRealizadas() + " vez(es). O máximo permitido é 2.");
        }

        // Regra 2: Verificar se o cliente possui devoluções em atraso
        Cliente cliente = emprestimo.getCliente();
        List<Emprestimo> emprestimosAtrasados = emprestimoRepository
                .findEmprestimosAtrasadosPorCliente(cliente, LocalDate.now());

        if (!emprestimosAtrasados.isEmpty()) {
            throw new RuntimeException(
                    "Renovação não permitida. O cliente '" + cliente.getNome() +
                    "' possui " + emprestimosAtrasados.size() + " empréstimo(s) com devolução em atraso.");
        }

        // Regra 3: Renovar — adiciona 7 dias a partir de hoje e incrementa o contador
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(7));
        emprestimo.setRenovacoesRealizadas(emprestimo.getRenovacoesRealizadas() + 1);

        return emprestimoRepository.save(emprestimo);
    }

    /**
     * Registra a devolução de um empréstimo.
     *
     * - Atualiza a dataDevolucao para a data atual.
     * - Marca o livro como disponível.
     * - Regra Crítica: Se a devolução ocorrer após a dataPrevistaDevolucao,
     *   o statusBloqueio do cliente é alterado para "BLOQUEADO".
     */
    @Transactional
    public Emprestimo registrarDevolucao(Long id) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado com id: " + id));

        // Atualiza a data de devolução para hoje
        LocalDate hoje = LocalDate.now();
        emprestimo.setDataDevolucao(hoje);
        emprestimo.setStatus("DEVOLVIDO");

        // Marca o livro como disponível
        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        // Regra Crítica: Bloqueio por atraso
        if (hoje.isAfter(emprestimo.getDataPrevistaDevolucao())) {
            Cliente cliente = emprestimo.getCliente();
            cliente.setStatusBloqueio("BLOQUEADO");
            clienteRepository.save(cliente);
        }

        return emprestimoRepository.save(emprestimo);
    }
}
