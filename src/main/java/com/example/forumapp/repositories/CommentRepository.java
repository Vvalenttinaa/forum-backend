package com.example.forumapp.repositories;

import com.example.forumapp.models.entities.CommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("SELECT c FROM CommentEntity c ORDER BY c.createdAt DESC")
    List<CommentEntity> findLatestCommentsByThema(Integer themaId, Pageable pageable);

    List<CommentEntity>findAllByThemaId(Integer id);
}
