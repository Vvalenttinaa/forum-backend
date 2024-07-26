package com.example.forumapp.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comment", schema = "forum", catalog = "")
public class CommentEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    @JoinColumn(name = "thema_id", referencedColumnName = "id")
    private ThemaEntity thema;

    @Basic
    @Column(name = "created_at")
    private Date createdAt;
    @Basic
    @Column(name = "approved")
    private Boolean approved;
    @Basic
    @Column(name = "blocked")
    private Boolean blocked;

}
