package com.ozcanpolat.audio_trancribe_backend.service;

import com.ozcanpolat.audio_trancribe_backend.exception.TranscriptionException;
import com.ozcanpolat.audio_trancribe_backend.exception.UnsupportedAudioFormatException;
import com.ozcanpolat.audio_trancribe_backend.util.AudioFormatUtil;
import com.ozcanpolat.audio_trancribe_backend.util.GoogleCredentialProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class TranscriptionService {

    public String transcribe(MultipartFile file, String languageCode) {
        try {
            String extension = AudioFormatUtil.getFileExtension(file);
            RecognitionConfig.AudioEncoding encoding = AudioFormatUtil.getAudioEncoding(extension);
            int sampleRate = AudioFormatUtil.getSampleRate(extension);

            ByteString audioBytes = ByteString.copyFrom(file.getBytes());

            RecognitionConfig.Builder configBuilder = RecognitionConfig.newBuilder()
                    .setEncoding(encoding)
                    .setLanguageCode(languageCode);

            if (sampleRate > 0) {
                configBuilder.setSampleRateHertz(sampleRate);
            }

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            SpeechSettings speechSettings = SpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(GoogleCredentialProvider.getCredentialsFromEnv()))
                    .build();

            try (SpeechClient speechClient = SpeechClient.create(speechSettings)) {
                RecognizeResponse response = speechClient.recognize(configBuilder.build(), audio);
                return response.getResultsList().stream()
                        .map(result -> result.getAlternativesList().get(0).getTranscript())
                        .reduce("", String::concat);
            }

        } catch (UnsupportedAudioFormatException e) {
            throw e;
        } catch (IOException e) {
            log.error("Dosya okunurken hata oluştu", e);
            throw new TranscriptionException("Ses dosyası okunamadı.", e);
        } catch (Exception e) {
            log.error("Transkripsiyon sırasında hata oluştu", e);
            throw new TranscriptionException("Transkripsiyon başarısız oldu.", e);
        }
    }
}
