package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.kurento.KurentoService;
import com.onix.springkurento.websocket.model.StopCommunicationOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public final class StopController {

    private final UserService userService;
    private final WebSocketMessagingService webSocketMessagingService;
    private final KurentoService kurentoService;

    public StopController(
            final UserService userService,
            final WebSocketMessagingService webSocketMessagingService,
            final KurentoService kurentoService
    ) {
        this.userService = userService;
        this.webSocketMessagingService = webSocketMessagingService;
        this.kurentoService = kurentoService;
    }

    @MessageMapping("/stop")
    void stop(final UserEntity principal) {
        log.info("Received message: []");

        UserSession user = this.userService.getById(principal.getId());

        stop(principal.getId());
        releasePipeline(user);
    }

    private void stop(final Integer userId) {
        UserSession stopperUser = this.userService.getById(userId);
        if (stopperUser != null) {
            UserSession stoppedUser = stopperUser.getCallingFrom() != null
                    ? this.userService.getByName(stopperUser.getCallingFrom()) : stopperUser.getCallingTo() != null
                    ? this.userService.getByName(stopperUser.getCallingTo()) : null;

            if (stoppedUser != null) {
                this.webSocketMessagingService.sendToUser(
                        stoppedUser.getId(),
                        new StopCommunicationOutputMessage()
                );

                stoppedUser.clear();
            }

            stopperUser.clear();
        }
    }

    private void releasePipeline(final UserSession session) {
        this.kurentoService.releasePipeline(session.getId());

        session.setWebRtcEndpoint(null);
        session.setPlayingWebRtcEndpoint(null);

        UserSession stoppedUser = session.getCallingFrom() != null
                ? this.userService.getByName(session.getCallingFrom())
                : this.userService.getByName(session.getCallingTo());

        stoppedUser.setWebRtcEndpoint(null);
        stoppedUser.setPlayingWebRtcEndpoint(null);
    }

}
