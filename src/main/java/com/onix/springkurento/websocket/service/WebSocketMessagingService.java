package com.onix.springkurento.websocket.service;

import com.onix.springkurento.consts.Destination;
import com.onix.springkurento.websocket.model.OutputMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public final class WebSocketMessagingService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketMessagingService(final SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToUser(final Integer userId, final OutputMessage message) {
        final String destination = Destination.TOPIC + "/" + userId;

        this.messagingTemplate.convertAndSend(destination, message);

        log.info("Send to user [{}] with message [{}]", userId, message);
    }

}
