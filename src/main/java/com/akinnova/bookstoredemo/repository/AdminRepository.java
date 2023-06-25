package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    //@Query("select count((u) > 0) from AdminEntity u where u.username = ?1 or u.email = ?2")
    Boolean existsByUsernameOrEmail(String username, String email);

    //@Query("select u from AdminEntity u where u.username = ?1 or u.email = ?2")
    Optional<AdminEntity> findByUsernameOrEmail(String username, String email);
}
