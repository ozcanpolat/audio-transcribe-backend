package com.ozcanpolat.audio_trancribe_backend.util;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GoogleCredentialProvider {

    public static GoogleCredentials getCredentialsFromEnv() throws IOException {
        String credentialsJson = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");

        if (credentialsJson == null || credentialsJson.isBlank()) {
            throw new IllegalStateException("GOOGLE_APPLICATION_CREDENTIALS_JSON environment variable is not set");
        }

        return GoogleCredentials.fromStream(new ByteArrayInputStream(
                credentialsJson.getBytes(StandardCharsets.UTF_8)
        ));
    }
}
