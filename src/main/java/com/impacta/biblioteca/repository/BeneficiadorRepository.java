package com.impacta.biblioteca.repository;

import com.impacta.biblioteca.model.Beneficiador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiadorRepository extends JpaRepository<Beneficiador, Long> {
}
