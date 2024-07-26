package com.example.forumapp.services;

import com.example.forumapp.models.dtos.Permission;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.models.responses.LoginResponse;

import java.util.List;

public interface UserService {
    Boolean approve(Integer id);
    Boolean block(Integer id);
    List<User>findAll();
    void addRole(Integer userId, Integer roleId);
    void addPermission(Integer userId, Integer permissionId);
    List<Permission>findPermissions();
    public LoginResponse login(String username);
}
