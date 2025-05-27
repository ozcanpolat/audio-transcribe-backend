package com.ozcanpolat.audio_trancribe_backend.util;

import com.ozcanpolat.audio_trancribe_backend.exception.UnsupportedAudioFormatException;
import com.google.cloud.speech.v1.RecognitionConfig;

import org.springframework.web.multipart.MultipartFile;

public class AudioFormatUtil {

    public static String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.contains(".")) {
            throw new UnsupportedAudioFormatException("Dosya uzantısı bulunamadı.");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    public static RecognitionConfig.AudioEncoding getAudioEncoding(String extension) {
        return switch (extension) {
            case "mp3" -> RecognitionConfig.AudioEncoding.MP3;
            case "wav" -> RecognitionConfig.AudioEncoding.LINEAR16;
            default -> throw new UnsupportedAudioFormatException("Sadece mp3 ve wav dosyaları desteklenmektedir.");
        };
    }

    public static int getSampleRate(String extension) {
        return switch (extension) {
            case "mp3" -> 0;       // Optional
            case "wav" -> 16000;
            default -> 0;
        };
    }
}

