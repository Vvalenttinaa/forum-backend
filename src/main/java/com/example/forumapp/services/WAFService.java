package com.example.forumapp.services;

import org.apache.coyote.BadRequestException;
import org.springframework.validation.BindingResult;

public interface WAFService {
    public void validation(BindingResult bindingResult) throws BadRequestException;
}
