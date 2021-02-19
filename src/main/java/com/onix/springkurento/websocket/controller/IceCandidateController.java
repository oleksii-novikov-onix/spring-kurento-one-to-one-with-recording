package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.websocket.model.IceCandidateInputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Slf4j
@Controller
public final class IceCandidateController {

    private final UserService userService;

    public IceCandidateController(final UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/ice-candidate")
    void iceCandidate(final @Payload IceCandidateInputMessage message, final UserEntity principal) {
        log.info("Received message: [{}]", message);

        UserSession user = this.userService.getById(principal.getId());

        if (Objects.nonNull(user)) {
            user.addCandidate(message.getCandidate());
        }
    }

}
