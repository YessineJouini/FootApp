package com.footapp.reservation.controller;

import com.footapp.reservation.model.Complexe;
import com.footapp.reservation.model.Terrain;
import com.footapp.reservation.repository.ComplexeRepository;
import com.footapp.reservation.repository.TerrainRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final TerrainRepository terrainRepo;
    private final ComplexeRepository complexeRepo;

    // ------- TERRAIN MANAGEMENT -------

    // GET all terrains
    @GetMapping("/terrains")
    public ResponseEntity<List<Terrain>> getAllTerrains() {
        return ResponseEntity.ok(terrainRepo.findAll());
    }
    @PostMapping("/terrains")
    public ResponseEntity<Terrain> createTerrain(@RequestBody Terrain terrain) {
        if (terrain.getComplexe() != null && terrain.getComplexe().getId() != null) {
            Optional<Complexe> complexe = complexeRepo.findById(terrain.getComplexe().getId());
            terrain.setComplexe(complexe.orElse(null));
        }
        return ResponseEntity.ok(terrainRepo.save(terrain));
    }

    @PutMapping("/terrains/{id}")
    public ResponseEntity<Terrain> updateTerrain(@PathVariable Long id, @RequestBody Terrain updatedTerrain) {
        return terrainRepo.findById(id)
                .map(terrain -> {
                    terrain.setNom(updatedTerrain.getNom());
                    terrain.setType(updatedTerrain.getType());
                    terrain.setCapacite(updatedTerrain.getCapacite());
                    if (updatedTerrain.getComplexe() != null && updatedTerrain.getComplexe().getId() != null) {
                        Optional<Complexe> complexe = complexeRepo.findById(updatedTerrain.getComplexe().getId());
                        terrain.setComplexe(complexe.orElse(terrain.getComplexe()));
                    }
                    return ResponseEntity.ok(terrainRepo.save(terrain));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/terrains/{id}")
    public ResponseEntity<?> deleteTerrain(@PathVariable Long id) {
        return terrainRepo.findById(id)
                .map(terrain -> {
                    terrainRepo.delete(terrain);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ------- COMPLEXE MANAGEMENT -------

    @GetMapping("/complexes")
    public ResponseEntity<List<Complexe>> getAllComplexes() {
        return ResponseEntity.ok(complexeRepo.findAll());
    }

    @GetMapping("/complexes/{id}")
    public ResponseEntity<Complexe> getComplexeById(@PathVariable Long id) {
        return complexeRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/complexes")
    public ResponseEntity<Complexe> createComplexe(@RequestBody Complexe complexe) {
        return ResponseEntity.ok(complexeRepo.save(complexe));
    }

    @PutMapping("/complexes/{id}")
    public ResponseEntity<Complexe> updateComplexe(@PathVariable Long id, @RequestBody Complexe updatedComplexe) {
        return complexeRepo.findById(id)
                .map(complexe -> {
                    complexe.setNom(updatedComplexe.getNom());
                    complexe.setAdresse(updatedComplexe.getAdresse());
                    return ResponseEntity.ok(complexeRepo.save(complexe));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/complexes/{id}")
    public ResponseEntity<?> deleteComplexe(@PathVariable Long id) {
        return complexeRepo.findById(id)
                .map(complexe -> {
                    complexeRepo.delete(complexe);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
