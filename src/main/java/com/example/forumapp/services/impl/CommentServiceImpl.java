package com.example.forumapp.services.impl;

import com.example.forumapp.exceptions.NotFoundException;
import com.example.forumapp.models.dtos.Comment;
import com.example.forumapp.models.entities.CommentEntity;
import com.example.forumapp.models.requests.CommentRequest;
import com.example.forumapp.repositories.CommentRepository;
import com.example.forumapp.repositories.ThemaRepository;
import com.example.forumapp.services.CommentService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ThemaRepository themaRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public Comment getById(Integer id) {
        return modelMapper.map(commentRepository.findById(id).orElseThrow(NotFoundException::new), Comment.class);
    }

    @Override
    public List<Comment> getAllByThema(Integer id) {
        List<CommentEntity> commentEntities = commentRepository.findAllByThemaId(id);
        List<Comment> comments = new ArrayList<>();
        for (CommentEntity comm:commentEntities
             ) {
            comments.add(modelMapper.map(comm, Comment.class));
        }
        return comments;
    }

    @Override
    public List<Comment> getLastByThema(Integer id, Integer limit){
        Pageable pageable = PageRequest.of(0, limit);
        List<CommentEntity> commentEntities = commentRepository.findLatestCommentsByThema(id, pageable);
        List<Comment> comments = new ArrayList<>();
        for (CommentEntity commentEntity:commentEntities
             ) {
                comments.add(modelMapper.map(commentEntity, Comment.class));
        }
        return comments;
    }
    @Override
    public Comment addComment(CommentRequest commentRequest){
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(commentRequest.getContent());
        commentEntity.setThema(themaRepository.findById(commentRequest.getThemaId()).orElseThrow(NotFoundException::new));
        commentEntity.setUserId(commentRequest.getUserId());
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        commentEntity.setCreatedAt(date);
        commentEntity.setApproved(false);
        commentEntity.setBlocked(false);
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        return modelMapper.map(commentEntity, Comment.class);
    }

    @Override
    public Boolean approve(Integer id, Boolean value) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentEntity.setApproved(value);
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        return commentEntity.getApproved();
    }

    @Override
    public Boolean block(Integer id, Boolean value) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(NotFoundException::new);
        commentEntity.setBlocked(value);
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        return commentEntity.getBlocked();

    }
}
