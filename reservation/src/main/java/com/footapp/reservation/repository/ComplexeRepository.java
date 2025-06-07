package com.footapp.reservation.repository;

import com.footapp.reservation.model.Complexe;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComplexeRepository extends JpaRepository<Complexe, Long> {
    Optional<Complexe> findByNom(String nom);
}