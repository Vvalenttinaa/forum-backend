package com.example.forumapp.repositories;

import com.example.forumapp.models.entities.BlockedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedRepository extends JpaRepository<BlockedEntity, Integer> {
    public Boolean existsByToken(String token);
}
