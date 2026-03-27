package com.impacta.biblioteca.service;

import com.impacta.biblioteca.model.Ticket;
import com.impacta.biblioteca.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TicketEmailService {

    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate;

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from.email:BiblioTech <onboarding@resend.dev>}")
    private String fromEmail;

    public TicketEmailService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.restTemplate = new RestTemplate();
    }

    public void enviarRelatorioTicketsAbertos(String emailDestino) {
        List<Ticket> ticketsAbertos = ticketRepository.findByStatus("ABERTO");

        StringBuilder corpo = new StringBuilder();
        corpo.append("=== Relatório de Tickets Abertos - BiblioTech ===\n\n");

        if (ticketsAbertos.isEmpty()) {
            corpo.append("Nenhum ticket aberto no momento.\n");
        } else {
            corpo.append("Total de tickets abertos: ").append(ticketsAbertos.size()).append("\n\n");
            for (Ticket ticket : ticketsAbertos) {
                corpo.append("─────────────────────────────────────\n");
                corpo.append("ID: ").append(ticket.getId()).append("\n");
                corpo.append("Título: ").append(ticket.getTitulo()).append("\n");
                corpo.append("Categoria: ").append(ticket.getCategoria()).append("\n");
                corpo.append("Prioridade: ").append(ticket.getPrioridade()).append("\n");
                corpo.append("Data de Criação: ").append(ticket.getDataCriacao()).append("\n");
                corpo.append("Descrição: ").append(ticket.getDescricao()).append("\n\n");
            }
        }

        // Enviar via Resend HTTP API (funciona no Railway sem bloqueio SMTP)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(resendApiKey);

        Map<String, Object> body = Map.of(
                "from", fromEmail,
                "to", List.of(emailDestino),
                "subject", "Relatório de Tickets Abertos - BiblioTech",
                "text", corpo.toString()
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api.resend.com/emails", request, String.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao enviar email via Resend: " + response.getBody());
        }
    }
}
