package com.footapp.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    
    @Column(name = "heure_debut")
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin") 
    private LocalTime heureFin;
    
    private String status; // "PENDING", "CONFIRMED", "CANCELLED"
    
    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    
    @ManyToOne
    @JoinColumn(name = "terrain_id")
    private Terrain terrain;
    
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Paiement paiement;
    
    // Helper methods
    public boolean isActive() {
        return "CONFIRMED".equals(status) || "PENDING".equals(status);
    }
    
    public boolean hasTimeConflict(LocalTime startTime, LocalTime endTime) {
        return !(endTime.isBefore(this.heureDebut) || startTime.isAfter(this.heureFin));
    }
}