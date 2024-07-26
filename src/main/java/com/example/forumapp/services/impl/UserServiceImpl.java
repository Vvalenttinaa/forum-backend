package com.example.forumapp.services.impl;

import com.example.forumapp.config.JwtAuthenticationFilter;
import com.example.forumapp.exceptions.NotFoundException;
import com.example.forumapp.models.dtos.Permission;
import com.example.forumapp.models.dtos.Role;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.models.entities.PermissionEntity;
import com.example.forumapp.models.entities.UserEntity;
import com.example.forumapp.models.responses.LoginResponse;
import com.example.forumapp.repositories.PermissionRepository;
import com.example.forumapp.repositories.RoleRepository;
import com.example.forumapp.repositories.UserRepository;
import com.example.forumapp.services.EmailService;
import com.example.forumapp.services.JwtService;
import com.example.forumapp.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    EmailService emailService;
    @Autowired
    JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    public Boolean approve(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setApproved(true);
        user = userRepository.saveAndFlush(user);
        emailService.sendMail("Your account has been approved.", user.getEmail());
        return user.getApproved();
    }

    @Override
    public Boolean block(Integer id) {
        UserEntity user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setBlocked(true);
        user = userRepository.saveAndFlush(user);
        return user.getBlocked();
    }
    @Override
    public List<User> findAll(){
        List<UserEntity> userEntities = userRepository.findAll();
        List<User> users = new ArrayList<>();
        for(UserEntity user: userEntities){
            User u = new User();
            u = mapper.map(user, User.class);
            u.setRole(mapper.map(roleRepository.findById(user.getRoleId()), Role.class));
            users.add(u);
        }
        return users;
    }

    @Override
    public void addRole(Integer userId, Integer roleId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userEntity.setRoleId(roleId);
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void addPermission(Integer userId, Integer permissionId){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        PermissionEntity permissionEntity = permissionRepository.findById(permissionId).orElseThrow(NotFoundException::new);
        userEntity.getPermissions().add(permissionEntity);
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public List<Permission> findPermissions(){
        List<PermissionEntity> p= permissionRepository.findAll();
        List<Permission> permissions = new ArrayList<>();
        for (PermissionEntity per: p
             ) {
            permissions.add(mapper.map(p, Permission.class));
        }
        return permissions;
    }

    @Override
    public LoginResponse login(String username){
        logger.info("od korisnika username je " + username);
        UserEntity u = userRepository.findByUsername(username).orElseThrow(NotFoundException::new);
        u.setActive(true);
        u = userRepository.saveAndFlush(u);
        var user = mapper.map(u, User.class);
        var role = mapper.map(roleRepository.findById(u.getRoleId()).orElseThrow(NotFoundException::new), Role.class);
        user.setRole(role);
        var jwt = jwtService.generateToken(user);
        logger.info("token je " + jwt);
        return LoginResponse.builder().token(jwt).build();
    }
}
