package com.onix.springkurento.kurento;

import com.onix.springkurento.websocket.UserSession;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.PlayerEndpoint;
import org.kurento.client.RecorderEndpoint;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

import static com.onix.springkurento.consts.MediaPipelineRecording.RECORDING_EXT;
import static com.onix.springkurento.consts.MediaPipelineRecording.RECORDING_PATH;

@Service
public final class KurentoService {

    private final ConcurrentHashMap<Integer, MediaPipeline> pipelines = new ConcurrentHashMap<>();

    private final KurentoClient kurentoClient;

    public KurentoService(final KurentoClient kurentoClient) {
        this.kurentoClient = kurentoClient;
    }

    public void put(final Integer userId, final MediaPipeline mediaPipeline) {
        this.pipelines.put(userId, mediaPipeline);
    }

    public void releasePipeline(final Integer userId) {
        if (this.pipelines.containsKey(userId)) {
            this.pipelines.get(userId).release();
            this.pipelines.remove(userId);
        }
    }

    public PlayMediaPipeline createPlayMediaPipeline(final UserSession user) {
        final MediaPipeline mediaPipeline = this.kurentoClient.createMediaPipeline();
        final WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(mediaPipeline).build();
        final PlayerEndpoint playerEndpoint = new PlayerEndpoint.Builder(
                mediaPipeline,
                this.getRecordingPath(user.getName())
        ).build();
        playerEndpoint.connect(webRtcEndpoint);

        return new PlayMediaPipeline(mediaPipeline, playerEndpoint, webRtcEndpoint);
    }

    public CallMediaPipeline createCallMediaPipeline(
            final UserSession from,
            final UserSession to
    ) {
        MediaPipeline pipeline = this.kurentoClient.createMediaPipeline();

        WebRtcEndpoint webRtcCaller = new WebRtcEndpoint.Builder(pipeline).build();
        WebRtcEndpoint webRtcCallee = new WebRtcEndpoint.Builder(pipeline).build();

        RecorderEndpoint recorderCaller = new RecorderEndpoint.Builder(pipeline, this.getRecordingPath(from.getName()))
                .withMediaProfile(from.getModeProfile())
                .build();
        RecorderEndpoint recorderCallee = new RecorderEndpoint.Builder(pipeline, this.getRecordingPath(to.getName()))
                .withMediaProfile(to.getModeProfile())
                .build();

        webRtcCaller.connect(webRtcCallee);
        webRtcCaller.connect(recorderCaller);

        webRtcCallee.connect(webRtcCaller);
        webRtcCallee.connect(recorderCallee);

        return new CallMediaPipeline(pipeline, webRtcCaller, webRtcCallee, recorderCaller, recorderCallee);
    }

    private String getRecordingPath(final String user) {
        return RECORDING_PATH + user + RECORDING_EXT;
    }

}
