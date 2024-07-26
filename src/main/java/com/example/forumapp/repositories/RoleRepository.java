package com.example.forumapp.repositories;

import com.example.forumapp.models.dtos.Role;
import com.example.forumapp.models.entities.RoleEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findById(Integer id);
    Optional<RoleEntity> findByName(String name);
}
