package com.impacta.biblioteca.controller;

import com.impacta.biblioteca.model.Ticket;
import com.impacta.biblioteca.repository.TicketRepository;
import com.impacta.biblioteca.service.TicketEmailService; // <-- Importação do serviço de e-mail
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final TicketEmailService ticketEmailService; // <-- Injetando o serviço

    // ─── GET — Listar todos os tickets ────────────────────────────
    @GetMapping
    public ResponseEntity<List<Ticket>> listarTodos() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    // ─── POST — Criar novo ticket ─────────────────────────────────
    @PostMapping
    public ResponseEntity<Ticket> criar(@RequestBody Ticket ticket) {
        ticket.setId(null);
        if (ticket.getStatus() == null) ticket.setStatus("ABERTO");
        Ticket ticketSalvo = ticketRepository.save(ticket);
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketSalvo);
    }

    // ─── PATCH — Atualizar status do ticket ────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<Ticket> atualizarStatus(@PathVariable Long id, @RequestBody String status) {
        return ticketRepository.findById(id)
                .map(ticketExistente -> {
                    String statusLimpo = status.replaceAll("^\"|\"$", "").trim();
                    ticketExistente.setStatus(statusLimpo);
                    Ticket ticketSalvo = ticketRepository.save(ticketExistente);
                    return ResponseEntity.ok(ticketSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── PATCH — Responder ticket ───────────────────────────────────
    @PatchMapping("/{id}/resposta")
    public ResponseEntity<Ticket> responderTicket(@PathVariable Long id, @RequestBody String resposta) {
        return ticketRepository.findById(id)
                .map(ticketExistente -> {
                    String respostaLimpa = resposta.replaceAll("^\"|\"$", "").trim();
                    ticketExistente.setResposta(respostaLimpa);
                    ticketExistente.setStatus("CONCLUIDO");
                    Ticket ticketSalvo = ticketRepository.save(ticketExistente);
                    return ResponseEntity.ok(ticketSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ─── POST — Enviar Relatório por E-mail ─────────────────────────
    @PostMapping("/relatorio")
    public ResponseEntity<Void> enviarRelatorio(@RequestBody String emailDestino) {
        // Limpa aspas caso o Front-end envie o e-mail em formato JSON/String "sujo"
        String emailLimpo = emailDestino.replaceAll("^\"|\"$", "").trim();
        ticketEmailService.enviarRelatorioTicketsAbertos(emailLimpo);
        return ResponseEntity.ok().build();
    }
}