package com.onix.springkurento.websocket;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.websocket.model.StopCommunicationOutputMessage;
import com.onix.springkurento.websocket.service.UserService;
import com.onix.springkurento.websocket.service.WebSocketMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Slf4j
@Component
public final class WebSocketEventListener {

    private final UserService userService;
    private final WebSocketMessagingService webSocketMessagingService;

    public WebSocketEventListener(
            final UserService userService,
            final WebSocketMessagingService webSocketMessagingService
    ) {
        this.userService = userService;
        this.webSocketMessagingService = webSocketMessagingService;
    }

    @EventListener
    public void disconnect(final SessionDisconnectEvent event) {
        if (Objects.nonNull(event.getUser())) {
            UserEntity userEntity = (UserEntity) event.getUser();

            log.info("Disconnect user [{}]", userEntity);

            UserSession stopperUser = this.userService.getById(userEntity.getId());

            if (stopperUser != null) {
                UserSession stoppedUser = (stopperUser.getCallingFrom() != null)
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

            this.userService.removeById(userEntity.getId());
        }
    }

}
