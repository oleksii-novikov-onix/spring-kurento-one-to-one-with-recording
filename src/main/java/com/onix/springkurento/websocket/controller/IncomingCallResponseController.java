package com.onix.springkurento.websocket.controller;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.kurento.CallMediaPipeline;
import com.onix.springkurento.kurento.KurentoService;
import com.onix.springkurento.websocket.model.CallResponseOutputMessage;
import com.onix.springkurento.websocket.model.IceCandidateOutputMessage;
import com.onix.springkurento.websocket.model.IncomingCallResponseInputMessage;
import com.onix.springkurento.websocket.model.StartCommunicationOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.UserSession;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public final class IncomingCallResponseController {

    private final UserService userService;
    private final KurentoService kurentoService;
    private final WebSocketMessagingService webSocketMessagingService;

    public IncomingCallResponseController(
            final UserService userService,
            final KurentoService kurentoService,
            final WebSocketMessagingService webSocketMessagingService
    ) {
        this.userService = userService;
        this.kurentoService = kurentoService;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    @MessageMapping("/incoming-call-response")
    void incomingCallResponse(
            final @Payload IncomingCallResponseInputMessage message,
            final UserEntity principal
    ) {
        log.info("Received message: [{}]", message);

        UserSession callee = this.userService.getById(principal.getId());
        callee.setMode(message.getMode());

        UserSession caller = this.userService.getByName(message.getFrom());
        String to = caller.getCallingTo();

        if ("accept".equals(message.getCallResponse())) {
            log.info("Accepted call from '{}' to '{}'", message.getFrom(), to);

            CallMediaPipeline callMediaPipeline = this.kurentoService.createCallMediaPipeline(caller, callee);
            this.kurentoService.put(caller.getId(), callMediaPipeline.getPipeline());
            this.kurentoService.put(callee.getId(), callMediaPipeline.getPipeline());

            callee.setWebRtcEndpoint(callMediaPipeline.getWebRtcCallee());
            callMediaPipeline.getWebRtcCallee().addIceCandidateFoundListener(
                    event -> this.webSocketMessagingService.sendToUser(
                            callee.getId(),
                            new IceCandidateOutputMessage(event.getCandidate())
                    ));

            String calleeSdpAnswer = callMediaPipeline.generateSdpAnswerForCallee(message.getSdpOffer());
            this.webSocketMessagingService.sendToUser(
                    callee.getId(),
                    new StartCommunicationOutputMessage(calleeSdpAnswer)
            );

            callMediaPipeline.getWebRtcCallee().gatherCandidates();

            String callerSdpOffer = this.userService.getByName(message.getFrom()).getSdpOffer();

            caller.setWebRtcEndpoint(callMediaPipeline.getWebRtcCaller());
            callMediaPipeline.getWebRtcCaller().addIceCandidateFoundListener(
                    event -> this.webSocketMessagingService.sendToUser(
                            caller.getId(),
                            new IceCandidateOutputMessage(event.getCandidate())
                    ));

            String callerSdpAnswer = callMediaPipeline.generateSdpAnswerForCaller(callerSdpOffer);

            this.webSocketMessagingService.sendToUser(
                    caller.getId(),
                    new CallResponseOutputMessage("accepted", callerSdpAnswer, null)
            );

            callMediaPipeline.getWebRtcCaller().gatherCandidates();
            callMediaPipeline.record();
        } else {
            this.webSocketMessagingService.sendToUser(
                    caller.getId(),
                    new CallResponseOutputMessage("rejected", null, null)
            );
        }
    }

}
