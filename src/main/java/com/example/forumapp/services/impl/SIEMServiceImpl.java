package com.example.forumapp.services.impl;

import com.example.forumapp.models.entities.SiemEntity;
import com.example.forumapp.repositories.SIEMRepository;
import com.example.forumapp.services.SIEMService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Transactional
@AllArgsConstructor
public class SIEMServiceImpl implements SIEMService {
    private final SIEMRepository siemRepository;

    @Override
    public void log(String log) {
        SiemEntity siemEntity = new SiemEntity();
        siemEntity.setDate(new Date());
        siemEntity.setContent(log);
        siemRepository.saveAndFlush(siemEntity);

    }
}
