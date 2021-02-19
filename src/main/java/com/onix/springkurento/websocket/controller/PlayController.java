package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.kurento.KurentoService;
import com.onix.springkurento.kurento.PlayMediaPipeline;
import com.onix.springkurento.websocket.model.IceCandidateOutputMessage;
import com.onix.springkurento.websocket.model.PlayEndOutputMessage;
import com.onix.springkurento.websocket.model.PlayInputMessage;
import com.onix.springkurento.websocket.model.PlayResponseOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Slf4j
@Controller
public final class PlayController {

    private final UserService userService;
    private final KurentoService kurentoService;
    private final WebSocketMessagingService webSocketMessagingService;

    public PlayController(
            final UserService userService,
            final KurentoService kurentoService,
            final WebSocketMessagingService webSocketMessagingService
    ) {
        this.userService = userService;
        this.kurentoService = kurentoService;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    @MessageMapping("/play")
    void play(
            final @Payload PlayInputMessage message,
            final UserEntity principal
    ) {
        log.info("Received message: [{}]", message);

        UserSession currentUserSession = this.userService.getById(principal.getId());

        PlayResponseOutputMessage response = new PlayResponseOutputMessage();

        if (Objects.nonNull(currentUserSession)) {
            UserSession userSession = this.userService.getByName(message.getUser());

            if (Objects.nonNull(userSession)) {
                final PlayMediaPipeline playMediaPipeline = this.kurentoService.createPlayMediaPipeline(userSession);

                playMediaPipeline.getPlayerEndpoint().addErrorListener(event -> {
                    log.error("ErrorEvent: {}", event.getDescription());
                    this.webSocketMessagingService.sendToUser(currentUserSession.getId(), new PlayEndOutputMessage());
                    playMediaPipeline.end();
                });


                currentUserSession.setPlayingWebRtcEndpoint(playMediaPipeline.getWebRtcEndpoint());

                playMediaPipeline.getPlayerEndpoint().addEndOfStreamListener(event -> {
                    releasePipeline(currentUserSession);
                    this.webSocketMessagingService.sendToUser(currentUserSession.getId(), new PlayEndOutputMessage());
                    playMediaPipeline.end();
                });

                playMediaPipeline.getWebRtcEndpoint().addIceCandidateFoundListener(
                        event -> this.webSocketMessagingService.sendToUser(
                                currentUserSession.getId(),
                                new IceCandidateOutputMessage(event.getCandidate())
                        ));

                String sdpAnswer = playMediaPipeline.generateSdpAnswer(message.getSdpOffer());

                response.setResponse("accepted");
                response.setSdpAnswer(sdpAnswer);

                playMediaPipeline.play();

                this.kurentoService.put(currentUserSession.getId(), playMediaPipeline.getMediaPipeline());

                this.webSocketMessagingService.sendToUser(currentUserSession.getId(), response);

                playMediaPipeline.getWebRtcEndpoint().gatherCandidates();
            } else {
                response.setResponse("rejected");
                response.setError("No recording for user '" + message.getUser()
                        + "'. Please type a correct user in the 'Peer' field.");

                this.webSocketMessagingService.sendToUser(currentUserSession.getId(), response);
            }
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
