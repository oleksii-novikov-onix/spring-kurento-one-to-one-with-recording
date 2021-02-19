package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.websocket.model.CallInputMessage;
import com.onix.springkurento.websocket.model.CallResponseOutputMessage;
import com.onix.springkurento.websocket.model.IncomingCallOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public final class CallController {

    private final UserService userService;
    private final WebSocketMessagingService webSocketMessagingService;

    public CallController(final UserService userService, final WebSocketMessagingService webSocketMessagingService) {
        this.userService = userService;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    @MessageMapping("/call")
    void call(final @Payload CallInputMessage message, final UserEntity principal) {
        log.info("Received message: [{}]", message);

        UserSession caller = this.userService.getById(principal.getId());

        if (this.userService.exists(message.getTo())) {
            caller.setSdpOffer(message.getSdpOffer());
            caller.setCallingTo(message.getTo());
            caller.setMode(message.getMode());

            UserSession callee = this.userService.getByName(message.getTo());
            callee.setCallingFrom(message.getFrom());

            this.webSocketMessagingService.sendToUser(
                    callee.getId(),
                    new IncomingCallOutputMessage(message.getFrom())
            );
        } else {
            this.webSocketMessagingService.sendToUser(
                    caller.getId(),
                    new CallResponseOutputMessage(
                            "rejected",
                            null,
                            "user '" + message.getTo() + "' is not registered"
                    )
            );
        }
    }

}
