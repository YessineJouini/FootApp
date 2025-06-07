package com.footapp.reservation.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Terrain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String type; // e.g., "Football", "Basketball", "Tennis"
    private int capacite;
    
    @ManyToOne
    @JoinColumn(name = "complexe_id")
    private Complexe complexe;
    
    @OneToMany(mappedBy = "terrain", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservations;
    
    // Helper method to check if terrain is available (basic)
    public boolean isAvailable() {
        // For now, always return true. 
        // Later we'll check against reservations for specific date/time
        return true;
    }
}