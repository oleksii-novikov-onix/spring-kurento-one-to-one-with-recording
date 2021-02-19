package com.onix.springkurento.websocket.model;

import com.onix.springkurento.websocket.consts.OutputMessageId;
import lombok.Getter;
import lombok.ToString;
import org.kurento.client.IceCandidate;

@Getter
@ToString(exclude = {"candidate"})
public class IceCandidateOutputMessage implements OutputMessage {

    private final String id = OutputMessageId.ICE_CANDIDATE;
    private IceCandidate candidate;

    public IceCandidateOutputMessage(final IceCandidate candidate) {
        this.candidate = candidate;
    }

}
