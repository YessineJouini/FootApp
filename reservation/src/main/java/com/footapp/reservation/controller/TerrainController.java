package com.footapp.reservation.controller;

import com.footapp.reservation.model.*;
import com.footapp.reservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/terrains")
@RequiredArgsConstructor
public class TerrainController {

    private final TerrainRepository terrainRepo;
    private final ComplexeRepository complexeRepo;

    // GET all terrains
    @GetMapping
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainRepo.findAll());
    }

    // GET terrain by ID
    @GetMapping("/{id}")
    public ResponseEntity<Terrain> getTerrainById(@PathVariable Long id) {
        return terrainRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // GET terrains by complexe
    @GetMapping("/complexe/{complexeId}")
    public ResponseEntity<List<Terrain>> getTerrainsByComplexe(@PathVariable Long complexeId) {
        return ResponseEntity.ok(terrainRepo.findByComplexeId(complexeId));
    }

    // SEARCH terrains by name
    @GetMapping("/search")
    public ResponseEntity<List<Terrain>> searchTerrains(@RequestParam String nom) {
        return ResponseEntity.ok(terrainRepo.findByNomContainingIgnoreCase(nom));
    }

    // POST create new terrain
    @PostMapping
    public ResponseEntity<Terrain> createTerrain(@RequestBody Terrain terrain) {
        // If complexe ID is provided, link it
        if (terrain.getComplexe() != null && terrain.getComplexe().getId() != null) {
            Optional<Complexe> complexe = complexeRepo.findById(terrain.getComplexe().getId());
            terrain.setComplexe(complexe.orElse(null));
        }
        return ResponseEntity.ok(terrainRepo.save(terrain));
    }

    // PUT update terrain
    @PutMapping("/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain updatedTerrain) {
        return terrainRepo.findById(id)
            .map(terrain -> {
                terrain.setNom(updatedTerrain.getNom());
                terrain.setType(updatedTerrain.getType());
                terrain.setCapacite(updatedTerrain.getCapacite());
                
                // Update complexe if provided
                if (updatedTerrain.getComplexe() != null && updatedTerrain.getComplexe().getId() != null) {
                    Optional<Complexe> complexe = complexeRepo.findById(updatedTerrain.getComplexe().getId());
                    terrain.setComplexe(complexe.orElse(terrain.getComplexe()));
                }
                
                return ResponseEntity.ok(terrainRepo.save(terrain));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE terrain
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerrain(@PathVariable Long id) {
        return terrainRepo.findById(id)
            .map(terrain -> {
                terrainRepo.delete(terrain);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}