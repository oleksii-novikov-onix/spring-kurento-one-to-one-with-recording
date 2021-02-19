package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.websocket.model.RegisterInputMessage;
import com.onix.springkurento.websocket.model.RegisterOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public final class RegisterController {

    private final UserService userRegistry;
    private final WebSocketMessagingService webSocketMessagingService;

    public RegisterController(
            final UserService userRegistry,
            final WebSocketMessagingService webSocketMessagingService
    ) {
        this.userRegistry = userRegistry;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    @MessageMapping("/register")
    void register(final @Payload RegisterInputMessage message, final UserEntity userEntity) {
        log.info("Received message [{}]", message);

        UserSession caller = new UserSession(userEntity.getId(), userEntity.getName());
        String response = "accepted";
        if (this.userRegistry.existsById(userEntity.getId())) {
            response = "rejected: user '" + userEntity.getId() + "' already in the call";
        } else {
            this.userRegistry.register(caller);
        }

        this.webSocketMessagingService.sendToUser(
                caller.getId(),
                new RegisterOutputMessage(response)
        );
    }

}
