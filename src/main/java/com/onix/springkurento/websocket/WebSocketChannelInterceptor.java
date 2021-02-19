package com.onix.springkurento.websocket;

import com.onix.springkurento.entity.UserEntity;
import com.onix.springkurento.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Lazy
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private final UserRepository userRepository;

    public WebSocketChannelInterceptor(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(final @Nullable Message<?> message, final MessageChannel channel) {
        if (Objects.nonNull(message)) {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            if (Objects.nonNull(accessor) && StompCommand.CONNECT.equals(accessor.getCommand())) {
                String userId = accessor.getFirstNativeHeader("user-id");

                if (Objects.nonNull(userId)) {
                    Optional<UserEntity> optionalUserEntity = this.userRepository.findById(Integer.parseInt(userId));
                    optionalUserEntity.ifPresent(accessor::setUser);
                }
            }
        }

        return message;
    }

}
