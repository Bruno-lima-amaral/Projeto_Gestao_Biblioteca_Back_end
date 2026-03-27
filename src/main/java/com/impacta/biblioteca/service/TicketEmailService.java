package com.impacta.biblioteca.service;

import com.impacta.biblioteca.model.Ticket;
import com.impacta.biblioteca.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketEmailService {

    private final TicketRepository ticketRepository;
    private final JavaMailSender mailSender;

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

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setTo(emailDestino);
        mensagem.setSubject("Relatório de Tickets Abertos - BiblioTech");
        mensagem.setText(corpo.toString());

        mailSender.send(mensagem);
    }
}
