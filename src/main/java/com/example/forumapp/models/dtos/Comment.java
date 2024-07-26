package com.example.forumapp.models.dtos;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class Comment {
    private Integer id;
    private String content;
    private Integer userId;
    private Integer themaId;
    private Timestamp createdAt;
    private Boolean approved;
    private Boolean blocked;

}
