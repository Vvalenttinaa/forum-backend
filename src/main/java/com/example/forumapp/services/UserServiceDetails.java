package com.example.forumapp.services;

import com.example.forumapp.exceptions.InvalidUsernameException;
import com.example.forumapp.exceptions.NotFoundException;
import com.example.forumapp.models.dtos.Role;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.models.entities.UserEntity;
import com.example.forumapp.repositories.RoleRepository;
import com.example.forumapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceDetails implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Role role = mapper.map(roleRepository.findById(userEntity.getRoleId())
                .orElseThrow(() -> new NotFoundException("Role not found")), Role.class);

        User user = mapper.map(userEntity, User.class);
        user.setRole(role);
        return user;
    }
}
