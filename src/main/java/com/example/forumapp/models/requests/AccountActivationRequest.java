package com.example.forumapp.models.requests;

import lombok.Data;

@Data
public class AccountActivationRequest {

    private String code;
    private String username;
}
