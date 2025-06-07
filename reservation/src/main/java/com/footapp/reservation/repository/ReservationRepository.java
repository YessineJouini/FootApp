package com.footapp.reservation.repository;

import com.footapp.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    List<Reservation> findByUtilisateurId(Long utilisateurId);
    
    List<Reservation> findByTerrainId(Long terrainId);
    
    List<Reservation> findByDate(LocalDate date);
    
    List<Reservation> findByStatus(String status);
    
    // Check for conflicting reservations
    @Query("SELECT r FROM Reservation r WHERE r.terrain.id = :terrainId " +
           "AND r.date = :date " +
           "AND r.status IN ('PENDING', 'CONFIRMED') " +
           "AND ((r.heureDebut <= :heureDebut AND r.heureFin > :heureDebut) " +
           "OR (r.heureDebut < :heureFin AND r.heureFin >= :heureFin) " +
           "OR (r.heureDebut >= :heureDebut AND r.heureFin <= :heureFin))")
    List<Reservation> findConflictingReservations(
        @Param("terrainId") Long terrainId,
        @Param("date") LocalDate date,
        @Param("heureDebut") LocalTime heureDebut,
        @Param("heureFin") LocalTime heureFin
    );
}