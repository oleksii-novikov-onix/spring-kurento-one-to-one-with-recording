package com.onix.springkurento.websocket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "sdpOffer")
public class PlayInputMessage implements InputMessage {

    private String user;
    private String sdpOffer;

}
