package com.footapp.reservation.controller;

import com.footapp.reservation.model.Complexe;
import com.footapp.reservation.repository.ComplexeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complexes")
@RequiredArgsConstructor
public class ComplexeController {

    private final ComplexeRepository complexeRepo;

    // GET all complexes
    @GetMapping
    public ResponseEntity<List<Complexe>> getAllComplexes() {
        return ResponseEntity.ok(complexeRepo.findAll());
    }

    // GET complexe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Complexe> getComplexeById(@PathVariable Long id) {
        return complexeRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // POST create new complexe
    @PostMapping
    public ResponseEntity<Complexe> createComplexe(@RequestBody Complexe complexe) {
        return ResponseEntity.ok(complexeRepo.save(complexe));
    }

    // PUT update complexe
    @PutMapping("/{id}")
    public ResponseEntity<Complexe> updateComplexe(@PathVariable Long id, @RequestBody Complexe updatedComplexe) {
        return complexeRepo.findById(id)
            .map(complexe -> {
                complexe.setNom(updatedComplexe.getNom());
                complexe.setAdresse(updatedComplexe.getAdresse());
                return ResponseEntity.ok(complexeRepo.save(complexe));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    // DELETE complexe
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComplexe(@PathVariable Long id) {
        return complexeRepo.findById(id)
            .map(complexe -> {
                complexeRepo.delete(complexe);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
}