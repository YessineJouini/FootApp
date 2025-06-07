package com.footapp.reservation.controller;

import com.footapp.reservation.model.Role;
import com.footapp.reservation.model.Utilisateur;
import com.footapp.reservation.repository.RoleRepository;
import com.footapp.reservation.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload, HttpSession session) {
        String email = payload.get("email");
        String password = payload.get("motDePasse");

        Optional<Utilisateur> user = utilisateurRepo.findByEmail(email);

        if (user.isPresent() && user.get().getMotDePasse().equals(password)) {
            session.setAttribute("user", user.get()); // ✅ store user in session
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Utilisateur user = (Utilisateur) session.getAttribute("user");
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // destroys session
        return ResponseEntity.ok("Logged out successfully");
    }


}
