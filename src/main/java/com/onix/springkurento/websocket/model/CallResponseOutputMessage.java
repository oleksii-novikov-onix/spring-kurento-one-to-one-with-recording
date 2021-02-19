package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "sdpAnswer")
public class CallResponseOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.CALL_RESPONSE;
    private String response;
    private String sdpAnswer;
    private String message;

    public CallResponseOutputMessage(final String response, final String sdpAnswer, final String message) {
        this.response = response;
        this.sdpAnswer = sdpAnswer;
        this.message = message;
    }

}
