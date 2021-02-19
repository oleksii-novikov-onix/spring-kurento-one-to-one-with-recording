package com.onix.springkurento.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.model.LoginRequest;
import com.onix.springkurento.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/login")
public final class LoginController {

    private final UserRepository userRepository;

    public LoginController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public UserEntity login(@RequestBody final LoginRequest loginRequest) {
        Optional<UserEntity> optionalUserEntity = this.userRepository.findByName(loginRequest.getUserName());

        UserEntity userEntity = optionalUserEntity.orElseGet(UserEntity::new);

        userEntity.setName(loginRequest.getUserName());

        this.userRepository.save(userEntity);

        return userEntity;
    }

}
