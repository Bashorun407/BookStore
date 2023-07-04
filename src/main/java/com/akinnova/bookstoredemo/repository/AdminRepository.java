package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<AdminEntity> findByUsername(String username);
    Optional<AdminEntity> findByEmail(String email);
}
