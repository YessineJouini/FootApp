package com.footapp.reservation.config;

import com.footapp.reservation.model.*;
import com.footapp.reservation.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final ComplexeRepository complexeRepo;
    private final TerrainRepository terrainRepo;

    @Override
    public void run(String... args) throws Exception {
        // Insert roles
        if (roleRepo.count() == 0) {
            roleRepo.save(new Role(null, "joueur"));
            roleRepo.save(new Role(null, "organisateur"));
        }
        
        // Insert sample complexes and terrains for testing
        if (complexeRepo.count() == 0) {
            // Create complexes
            Complexe complexe1 = complexeRepo.save(new Complexe(null, "Sports Center Tunis", "Avenue Habib Bourguiba, Tunis", null));
            Complexe complexe2 = complexeRepo.save(new Complexe(null, "Stade Municipal", "Rue de la République, Tunis", null));
            
            // Create terrains (without reservations list for now)
            Terrain terrain1 = new Terrain();
            terrain1.setNom("Terrain A");
            terrain1.setType("Football");
            terrain1.setCapacite(22);
            terrain1.setComplexe(complexe1);
            terrainRepo.save(terrain1);
            
            Terrain terrain2 = new Terrain();
            terrain2.setNom("Terrain B");
            terrain2.setType("Football");
            terrain2.setCapacite(18);
            terrain2.setComplexe(complexe1);
            terrainRepo.save(terrain2);
            
            Terrain terrain3 = new Terrain();
            terrain3.setNom("Court de Tennis");
            terrain3.setType("Tennis");
            terrain3.setCapacite(4);
            terrain3.setComplexe(complexe1);
            terrainRepo.save(terrain3);
            
            Terrain terrain4 = new Terrain();
            terrain4.setNom("Grand Terrain");
            terrain4.setType("Football");
            terrain4.setCapacite(30);
            terrain4.setComplexe(complexe2);
            terrainRepo.save(terrain4);
            
            Terrain terrain5 = new Terrain();
            terrain5.setNom("Petit Terrain");
            terrain5.setType("Football");
            terrain5.setCapacite(14);
            terrain5.setComplexe(complexe2);
            terrainRepo.save(terrain5);
        }
    }
}