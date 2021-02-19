package com.onix.springkurento.kurento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.kurento.client.MediaPipeline;
import org.kurento.client.PlayerEndpoint;
import org.kurento.client.WebRtcEndpoint;

@AllArgsConstructor
public final class PlayMediaPipeline {

    @Getter
    private final MediaPipeline mediaPipeline;
    @Getter
    private final PlayerEndpoint playerEndpoint;
    @Getter
    @Setter
    private WebRtcEndpoint webRtcEndpoint;

    public void play() {
        this.playerEndpoint.play();
    }

    public void end() {
        this.mediaPipeline.release();
        this.webRtcEndpoint = null;
    }

    public String generateSdpAnswer(final String sdpOffer) {
        return this.webRtcEndpoint.processOffer(sdpOffer);
    }

}
