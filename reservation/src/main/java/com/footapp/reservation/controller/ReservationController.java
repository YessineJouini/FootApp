package com.footapp.reservation.controller;

import com.footapp.reservation.model.*;
import com.footapp.reservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepo;
    private final TerrainRepository terrainRepo;
    private final UtilisateurRepository utilisateurRepo;
    private final PaiementRepository paiementRepo;

    // GET all reservations
    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationRepo.findAll());
    }

    // GET reservations by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationRepo.findByUtilisateurId(userId));
    }

    // GET reservations by terrain
    @GetMapping("/terrain/{terrainId}")
    public ResponseEntity<List<Reservation>> getReservationsByTerrain(@PathVariable Long terrainId) {
        return ResponseEntity.ok(reservationRepo.findByTerrainId(terrainId));
    }

    // Check availability for a terrain on specific date/time
    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam Long terrainId,
            @RequestParam String date,
            @RequestParam String heureDebut,
            @RequestParam String heureFin) {
        
        try {
            LocalDate reservationDate = LocalDate.parse(date);
            LocalTime startTime = LocalTime.parse(heureDebut);
            LocalTime endTime = LocalTime.parse(heureFin);
            
            List<Reservation> conflicts = reservationRepo.findConflictingReservations(
                terrainId, reservationDate, startTime, endTime);
            
            Map<String, Object> response = new HashMap<>();
            response.put("available", conflicts.isEmpty());
            response.put("conflictingReservations", conflicts.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // POST create new reservation
    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody Map<String, Object> request) {
        try {
            // Parse request data
            Long terrainId = Long.parseLong(request.get("terrainId").toString());
            Long utilisateurId = Long.parseLong(request.get("utilisateurId").toString());
            LocalDate date = LocalDate.parse(request.get("date").toString());
            LocalTime heureDebut = LocalTime.parse(request.get("heureDebut").toString());
            LocalTime heureFin = LocalTime.parse(request.get("heureFin").toString());
            
            // Validate terrain and user exist
            Optional<Terrain> terrain = terrainRepo.findById(terrainId);
            Optional<Utilisateur> utilisateur = utilisateurRepo.findById(utilisateurId);
            
            if (terrain.isEmpty() || utilisateur.isEmpty()) {
                return ResponseEntity.badRequest().body("Terrain or User not found");
            }
            
            // Check for conflicts
            List<Reservation> conflicts = reservationRepo.findConflictingReservations(
                terrainId, date, heureDebut, heureFin);
            
            if (!conflicts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Time slot not available - conflicts with existing reservation");
            }
            
            // Create reservation
            Reservation reservation = new Reservation();
            reservation.setTerrain(terrain.get());
            reservation.setUtilisateur(utilisateur.get());
            reservation.setDate(date);
            reservation.setHeureDebut(heureDebut);
            reservation.setHeureFin(heureFin);
            reservation.setStatus("PENDING");
            
            Reservation saved = reservationRepo.save(reservation);
            return ResponseEntity.ok(saved);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating reservation: " + e.getMessage());
        }
    }

    // PUT update reservation status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateReservationStatus(
            @PathVariable Long id, 
            @RequestBody Map<String, String> request) {
        
        return reservationRepo.findById(id)
            .map(reservation -> {
                String newStatus = request.get("status");
                if (Arrays.asList("PENDING", "CONFIRMED", "CANCELLED").contains(newStatus)) {
                    reservation.setStatus(newStatus);
                    return ResponseEntity.ok(reservationRepo.save(reservation));
                } else {
                    return ResponseEntity.badRequest().body("Invalid status");
                }
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE reservation (cancel)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) {
        return reservationRepo.findById(id)
            .map(reservation -> {
                reservation.setStatus("CANCELLED");
                reservationRepo.save(reservation);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}