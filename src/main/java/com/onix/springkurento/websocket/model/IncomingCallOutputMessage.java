package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class IncomingCallOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.INCOMING_CALL;
    private String from;

    public IncomingCallOutputMessage(final String from) {
        this.from = from;
    }

}
