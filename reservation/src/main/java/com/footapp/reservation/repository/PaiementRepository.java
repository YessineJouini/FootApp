package com.footapp.reservation.repository;

import com.footapp.reservation.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    
    Optional<Paiement> findByReservationId(Long reservationId);
    
    List<Paiement> findByStatus(String status);
    
    List<Paiement> findByModePaiement(String modePaiement);
}