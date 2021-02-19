package com.onix.springkurento.consts;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class MediaPipelineRecording {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-S");
    public static final String RECORDING_PATH = "file:///tmp/" + DATE_FORMAT.format(new Date()) + "-";
    public static final String RECORDING_EXT = ".webm";

    private MediaPipelineRecording() {

    }

}
