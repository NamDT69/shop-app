package com.project.shopapp.repository;

import com.project.shopapp.model.Role;
import com.project.shopapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
