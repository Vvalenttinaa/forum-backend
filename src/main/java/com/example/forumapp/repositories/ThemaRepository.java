package com.example.forumapp.repositories;

import com.example.forumapp.models.entities.ThemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemaRepository extends JpaRepository<ThemaEntity, Integer> {
}
