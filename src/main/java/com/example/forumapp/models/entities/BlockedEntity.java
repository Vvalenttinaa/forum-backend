package com.example.forumapp.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "blocked", schema = "forum", catalog = "")

public class BlockedEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "token")
    private String token;
}
