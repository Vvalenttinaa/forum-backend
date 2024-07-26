package com.example.forumapp.controllers;

import com.example.forumapp.config.JwtAuthenticationFilter;
import com.example.forumapp.models.dtos.Comment;
import com.example.forumapp.models.requests.CommentRequest;
import com.example.forumapp.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
public class ForumController {
    @Autowired
    CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @GetMapping("/comment/{id}")
    public Comment getCommentById(@PathVariable Integer id){
        return commentService.getById(id);
    }
    @GetMapping("comment/thema/{id}")
    public List<Comment> getCommentsByThema(@PathVariable Integer id){
        return commentService.getAllByThema(id);
    }
    @PreAuthorize("hasRole('admin') or hasRole('moderator') or hasRole('user')")
    @GetMapping("comments/thema/{id}")
    public List<Comment> getLastCommentsByThema(@PathVariable Integer id, @RequestParam(defaultValue = "20") int limit){
        return commentService.getLastByThema(id, limit);
    }
    @PostMapping("/comment")
    public void comment(@RequestBody CommentRequest commentRequest){
        commentService.addComment(commentRequest);
    }
    @PreAuthorize("hasRole('admin') or hasRole('moderator')")
    @PutMapping("/comment/approve/{id}")
    public Boolean approve(@PathVariable Integer id, @RequestParam Boolean value){
        return commentService.approve(id, value);
    }
    @PreAuthorize("hasRole('admin') or hasRole('moderator')")
    @PutMapping("/comment/block/{id}")
    public Boolean block(@PathVariable Integer id,  @RequestParam Boolean value){
        return commentService.block(id, value);
    }


}
