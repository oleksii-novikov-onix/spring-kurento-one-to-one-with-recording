package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RegisterOutputMessage implements OutputMessage {

    private final String id  = OutputMessageId.REGISTER;
    private String response;

    public RegisterOutputMessage(final String response) {
        this.response = response;
    }

}
