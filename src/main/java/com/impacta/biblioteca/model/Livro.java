package com.impacta.biblioteca.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @ManyToOne
    @JoinColumn(name = "beneficiador_id")
    private Beneficiador beneficiador;

    private String genero;

    @Column(unique = true)
    private String isbn;
}
