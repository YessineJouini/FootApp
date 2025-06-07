package com.footapp.reservation.repository;

import com.footapp.reservation.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TerrainRepository extends JpaRepository<Terrain, Long> {
    List<Terrain> findByNomContainingIgnoreCase(String nom);
    List<Terrain> findByType(String type);
    List<Terrain> findByComplexeId(Long complexeId);
    List<Terrain> findByCapaciteGreaterThanEqual(int capacite);
}