package com.impacta.biblioteca.service;

import com.impacta.biblioteca.model.Cliente;
import com.impacta.biblioteca.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // ─── Listar todos os clientes ────────────────────────────────

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // ─── Buscar cliente por ID ───────────────────────────────────

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // ─── Criar novo cliente ──────────────────────────────────────

    public Cliente criar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este e-mail.");
        }
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
        }

        cliente.setId(null); // Garante que será um novo registro
        return clienteRepository.save(cliente);
    }

    // ─── Editar cliente por ID ───────────────────────────────────

    public Optional<Cliente> editar(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(clienteExistente -> {
                    if (!clienteExistente.getEmail().equals(clienteAtualizado.getEmail()) &&
                            clienteRepository.existsByEmail(clienteAtualizado.getEmail())) {
                        throw new IllegalArgumentException("Já existe um cliente cadastrado com este e-mail.");
                    }
                    if (!clienteExistente.getCpf().equals(clienteAtualizado.getCpf()) &&
                            clienteRepository.existsByCpf(clienteAtualizado.getCpf())) {
                        throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
                    }

                    clienteExistente.setNome(clienteAtualizado.getNome());
                    clienteExistente.setEmail(clienteAtualizado.getEmail());
                    clienteExistente.setCpf(clienteAtualizado.getCpf());
                    clienteExistente.setTelefone(clienteAtualizado.getTelefone());
                    clienteExistente.setSexo(clienteAtualizado.getSexo());
                    clienteExistente.setDataNascimento(clienteAtualizado.getDataNascimento());
                    return clienteRepository.save(clienteExistente);
                });
    }

    // ─── Deletar cliente por ID ──────────────────────────────────

    public boolean deletar(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return true;
                })
                .orElse(false);
    }
}
