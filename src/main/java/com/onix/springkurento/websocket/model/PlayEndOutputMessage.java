package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PlayEndOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.PLAY_END;

}
