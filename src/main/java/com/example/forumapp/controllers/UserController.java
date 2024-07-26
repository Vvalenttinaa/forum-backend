package com.example.forumapp.controllers;

import com.example.forumapp.models.dtos.Permission;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    List<User> findAll(){
        return userService.findAll();
    }

    @PutMapping("/approve/{id}")
    Boolean approve(@PathVariable Integer id){
        return this.userService.approve(id);
    }

    @PutMapping("/block/{id}")
    Boolean block(@PathVariable Integer id){
        return this.userService.block(id);
    }

    @PutMapping("{userId}/role/{roleId}")
    public void addRole(@PathVariable Integer userId, @PathVariable Integer roleId){
        this.userService.addRole(userId, roleId);
    }
    @PostMapping("{userId}/permission/{permissionId}")
    public void addPermision(@PathVariable Integer userId, @PathVariable Integer permissionId){
        this.userService.addPermission(userId, permissionId);
    }

    @GetMapping("/permission")
    public List<Permission>getPermissions(){
        return userService.findPermissions();
    }
}
