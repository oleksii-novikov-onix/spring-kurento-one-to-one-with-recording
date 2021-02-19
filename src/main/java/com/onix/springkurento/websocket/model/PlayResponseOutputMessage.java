package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(exclude = "sdpAnswer")
@NoArgsConstructor
public class PlayResponseOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.PLAY_RESPONSE;
    @Setter
    private String response;
    @Setter
    private String sdpAnswer;
    @Setter
    private String error;

}
