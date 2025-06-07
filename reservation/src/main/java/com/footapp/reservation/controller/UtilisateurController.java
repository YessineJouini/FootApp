package com.footapp.reservation.controller;

import com.footapp.reservation.model.Role;
import com.footapp.reservation.model.Utilisateur;
import com.footapp.reservation.repository.RoleRepository;
import com.footapp.reservation.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepo;
    private final RoleRepository roleRepo;

    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody Utilisateur u) {
        Optional<Role> role = roleRepo.findByRoletype("joueur");
        u.setRole(role.orElse(null));
        Utilisateur saved = utilisateurRepo.save(u);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("motDePasse");

        Optional<Utilisateur> user = utilisateurRepo.findByEmail(email);

        if (user.isPresent() && user.get().getMotDePasse().equals(password)) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
