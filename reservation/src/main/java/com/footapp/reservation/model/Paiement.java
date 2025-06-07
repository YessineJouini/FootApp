package com.footapp.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int montant; // Amount in cents or smallest currency unit
    
    @Column(name = "date_paiement")
    private LocalDate datePaiement;
    
    @Column(name = "mode_paiement")
    private String modePaiement; // "CARD", "CASH", "TRANSFER"
    
    private String status; // "PENDING", "COMPLETED", "FAILED"
    
    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
    
    // Helper methods
    public boolean isCompleted() {
        return "COMPLETED".equals(status);
    }
    
    public double getMontantInDinars() {
        return montant / 100.0; // Convert cents to dinars
    }
}