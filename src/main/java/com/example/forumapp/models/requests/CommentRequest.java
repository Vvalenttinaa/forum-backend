package com.example.forumapp.models.requests;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.sql.Date;

@Data
public class CommentRequest {
    private String content;
    private Integer userId;
    private Integer themaId;
    private Date date;
    private Boolean approved;
    private Boolean blocked;
}
