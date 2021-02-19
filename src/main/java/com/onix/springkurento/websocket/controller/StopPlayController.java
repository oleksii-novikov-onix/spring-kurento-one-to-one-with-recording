package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.kurento.KurentoService;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public final class StopPlayController {

    private final UserService userService;
    private final KurentoService kurentoService;

    public StopPlayController(final UserService userService, final KurentoService kurentoService) {
        this.userService = userService;
        this.kurentoService = kurentoService;
    }

    @MessageMapping("/stop-play")
    void stopPlay(final UserEntity principal) {
        log.info("Received message: []");

        UserSession session = this.userService.getById(principal.getId());
        releasePipeline(session);
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
