package com.example.forumapp.repositories;

import com.example.forumapp.models.entities.SiemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SIEMRepository extends JpaRepository<SiemEntity, Integer> {
}
