package com.onix.springkurento.websocket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kurento.client.IceCandidate;

@Getter
@Setter
@ToString(exclude = {"candidate"})
public class IceCandidateInputMessage implements InputMessage {

    private IceCandidate candidate;

}
