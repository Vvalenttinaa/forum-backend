package com.example.forumapp.services;

import com.example.forumapp.models.dtos.Comment;
import com.example.forumapp.models.requests.CommentRequest;

import java.util.List;

public interface CommentService {
    Comment getById(Integer id);
    List<Comment>getAllByThema(Integer id);
    List<Comment>getLastByThema(Integer id, Integer limit);
    Comment addComment(CommentRequest commentRequest);
    Boolean approve(Integer id, Boolean value);
    Boolean block(Integer id, Boolean value);
}
