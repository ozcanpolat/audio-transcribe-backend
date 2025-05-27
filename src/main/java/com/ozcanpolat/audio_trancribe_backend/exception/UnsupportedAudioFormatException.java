package com.ozcanpolat.audio_trancribe_backend.exception;

public class UnsupportedAudioFormatException extends RuntimeException {
    public UnsupportedAudioFormatException(String message) {
        super(message);
    }
}

