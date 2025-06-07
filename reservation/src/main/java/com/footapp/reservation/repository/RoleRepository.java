package com.footapp.reservation.repository;

import com.footapp.reservation.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoletype(String roletype);
}
