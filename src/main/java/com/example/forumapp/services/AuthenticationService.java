package com.example.forumapp.services;

import com.example.forumapp.exceptions.InvalidUsernameException;
import com.example.forumapp.exceptions.NotFoundException;
import com.example.forumapp.models.dtos.Role;
import com.example.forumapp.models.dtos.User;
import com.example.forumapp.models.entities.UserEntity;
import com.example.forumapp.models.requests.AccountActivationRequest;
import com.example.forumapp.models.requests.LoginRequest;
import com.example.forumapp.models.requests.RegisterRequest;
import com.example.forumapp.models.responses.LoginResponse;
import com.example.forumapp.repositories.RoleRepository;
import com.example.forumapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private Map<String, String> codes = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public LoginResponse login(LoginRequest request) {
        logger.info("Authenticationg user "+ request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var userEntity = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new InvalidUsernameException("Invalid username or password."));
        var user = modelMapper.map(userEntity, User.class);
        var role = modelMapper.map(roleRepository.findById(userEntity.getRoleId()).orElseThrow(NotFoundException::new), Role.class);
        user.setRole(role);
        logger.info(user.getUsername() + " " + user.getPassword());
        if(!user.getActive()){
            sendActivationCode(user.getEmail(), user.getUsername());
            return LoginResponse.builder().token(null).build();
        }
        var jwt = jwtService.generateToken(user);
        logger.info("Generated JWT token for user: {}", jwt);
        logger.info(user.toString());

        return LoginResponse.builder().token(jwt).build();
    }

    public User register(RegisterRequest request){
        UserEntity entity = modelMapper.map(request, UserEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRoleId(2);
        entity.setApproved(false);
        entity.setBlocked(false);

        logger.info("Found " + entity.getRoleId());

        entity = userRepository.saveAndFlush(entity);
        logger.info("Saved " + entity.getRoleId());
        User u = modelMapper.map(entity, User.class);
        Role r = modelMapper.map(roleRepository.findById(entity.getRoleId()).orElseThrow(NotFoundException::new), Role.class);
        u.setRole(r);
        return u;
    }

    public void sendActivationCode(String email, String username) {
        SecureRandom secureRandom = new SecureRandom();
        String activationCode = String.valueOf(secureRandom.nextInt(9000) + 1000);
        while(codes.containsKey(activationCode))
        {
            activationCode=String.valueOf(secureRandom.nextInt(9000)+1000);
        }
        codes.put(username, activationCode);
        emailService.sendMail(activationCode, email);

    }

    public boolean activateAccount(AccountActivationRequest request) {
        logger.info("usao u auth service");
        return codes.containsKey(request.getUsername()) && codes.get(request.getUsername()).equals(request.getCode());
    }

}
