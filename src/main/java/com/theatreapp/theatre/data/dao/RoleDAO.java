package com.theatreapp.theatre.data.dao;

import com.theatreapp.theatre.data.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleDAO extends JpaRepository<Role, Long> {
    Role findByAuthority(String role);
}
