package com.footapp.reservation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Complexe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    private String adresse;
    
    @OneToMany(mappedBy = "complexe", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Terrain> terrains;
}