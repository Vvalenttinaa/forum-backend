package com.example.forumapp.services.impl;

import com.example.forumapp.services.BlackListService;
import com.example.forumapp.services.SIEMService;
import com.example.forumapp.services.WAFService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class WAFServiceImpl implements WAFService {

    private final HttpServletRequest request;

    private final SIEMService siemService;
    private final BlackListService blackListService;

    @Override
    public void validation(BindingResult bindingResult) throws BadRequestException {
        if(bindingResult.hasErrors()){
            StringBuilder errorBuilder = new StringBuilder("Address: " + request.getRemoteAddr() + ".Validation errors: ");

            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                errorBuilder.append(fieldError.getDefaultMessage()).append(", ");
            }
            errorBuilder.setLength(errorBuilder.length() - 2);
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String jwt = authorizationHeader.substring(7);
                System.out.println(errorBuilder.toString());
                blackListService.blockJWT(jwt);
            }
            siemService.log(errorBuilder.toString());
            throw new BadRequestException();

        }
    }
}
