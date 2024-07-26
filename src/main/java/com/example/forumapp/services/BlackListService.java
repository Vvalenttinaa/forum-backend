package com.example.forumapp.services;

public interface BlackListService {
    void blockJWT(String token);
    Boolean existToken(String token);
}
