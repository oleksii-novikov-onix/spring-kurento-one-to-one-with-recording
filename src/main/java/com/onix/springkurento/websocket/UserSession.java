package com.onix.springkurento.websocket;

import lombok.Getter;
import lombok.Setter;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaProfileSpecType;
import org.kurento.client.WebRtcEndpoint;

import java.util.ArrayList;
import java.util.List;

public final class UserSession {

    @Getter
    private final Integer id;
    @Getter
    private final String name;
    @Getter
    @Setter
    private String sdpOffer;
    @Setter
    private String mode;
    @Getter
    @Setter
    private String callingTo;
    @Getter
    @Setter
    private String callingFrom;
    @Getter
    @Setter
    private WebRtcEndpoint playingWebRtcEndpoint;
    private WebRtcEndpoint webRtcEndpoint;
    private final List<IceCandidate> iceCandidates = new ArrayList<>();

    public UserSession(final Integer id, final String name) {
        this.id = id;
        this.name = name;
    }

    public void setWebRtcEndpoint(final WebRtcEndpoint webRtcEndpoint) {
        this.webRtcEndpoint = webRtcEndpoint;

        if (this.webRtcEndpoint != null) {
            this.iceCandidates.forEach(iceCandidate -> this.webRtcEndpoint.addIceCandidate(iceCandidate));

            this.iceCandidates.clear();
        }
    }

    public void addCandidate(final IceCandidate candidate) {
        if (this.webRtcEndpoint != null) {
            this.webRtcEndpoint.addIceCandidate(candidate);
        } else {
            this.iceCandidates.add(candidate);
        }

        if (this.playingWebRtcEndpoint != null) {
            this.playingWebRtcEndpoint.addIceCandidate(candidate);
        }
    }

    public void clear() {
        this.webRtcEndpoint = null;
        this.iceCandidates.clear();
    }

    public MediaProfileSpecType getModeProfile() {
        switch (mode) {
            case "audio":
                return MediaProfileSpecType.WEBM_AUDIO_ONLY;
            case "video":
                return MediaProfileSpecType.WEBM_VIDEO_ONLY;
            default:
                return MediaProfileSpecType.WEBM;
        }
    }

}
