package com.impacta.biblioteca.repository;

import com.impacta.biblioteca.model.Cliente;
import com.impacta.biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    /**
     * Verifica se o cliente possui empréstimos em atraso:
     * dataDevolucao IS NULL (não devolvido) E dataPrevistaDevolucao < hoje.
     */
    @Query("SELECT e FROM Emprestimo e WHERE e.cliente = :cliente " +
           "AND e.dataDevolucao IS NULL " +
           "AND e.dataPrevistaDevolucao < :dataAtual")
    List<Emprestimo> findEmprestimosAtrasadosPorCliente(
            @Param("cliente") Cliente cliente,
            @Param("dataAtual") LocalDate dataAtual);
}
