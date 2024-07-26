package com.example.forumapp.services.impl;

import com.example.forumapp.models.entities.BlockedEntity;
import com.example.forumapp.repositories.BlockedRepository;
import com.example.forumapp.services.BlackListService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Transactional
public class BlackListServiceImpl implements BlackListService {
    private final BlockedRepository blockedRepository;

    @Override
    public void blockJWT(String token) {
        BlockedEntity blockedEntity = new BlockedEntity();
        blockedEntity.setToken(token);
        blockedRepository.saveAndFlush(blockedEntity);
    }

    @Override
    public Boolean existToken(String token){
        return !blockedRepository.existsByToken(token);
    }
}
