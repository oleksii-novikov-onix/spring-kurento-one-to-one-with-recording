package com.onix.springkurento.kurento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.kurento.client.MediaPipeline;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;

@ToString
@AllArgsConstructor
public final class CallMediaPipeline {

    @Getter
    private final MediaPipeline pipeline;
    @Getter
    private final WebRtcEndpoint webRtcCaller;
    @Getter
    private final WebRtcEndpoint webRtcCallee;
    private final RecorderEndpoint recorderCaller;
    private final RecorderEndpoint recorderCallee;

    public void record() {
        this.recorderCaller.record();
        this.recorderCallee.record();
    }

    public String generateSdpAnswerForCaller(final String sdpOffer) {
        return this.webRtcCaller.processOffer(sdpOffer);
    }

    public String generateSdpAnswerForCallee(final String sdpOffer) {
        return this.webRtcCallee.processOffer(sdpOffer);
    }

}
