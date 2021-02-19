package com.onix.springkurento.websocket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "sdpOffer")
public class IncomingCallResponseInputMessage implements InputMessage {

    private String callResponse;
    private String from;
    private String sdpOffer;
    private String mode;

}
