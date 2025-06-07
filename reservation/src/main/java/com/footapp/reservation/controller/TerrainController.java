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
  
}