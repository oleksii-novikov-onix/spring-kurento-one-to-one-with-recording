package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(exclude = "sdpAnswer")
public class StartCommunicationOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.START_COMMUNICATION;
    private String sdpAnswer;

    public StartCommunicationOutputMessage(final String sdpAnswer) {
        this.sdpAnswer = sdpAnswer;
    }

}
