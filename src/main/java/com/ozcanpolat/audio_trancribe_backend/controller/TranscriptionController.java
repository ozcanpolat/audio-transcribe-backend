package com.ozcanpolat.audio_trancribe_backend.controller;

import com.ozcanpolat.audio_trancribe_backend.service.TranscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/transcribe")
@RequiredArgsConstructor
public class TranscriptionController {

    private final TranscriptionService transcriptionService;

    @PostMapping
    public ResponseEntity<String> transcribeAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "lang", defaultValue = "en-US") String languageCode) {
        String transcript = transcriptionService.transcribe(file, languageCode);
        return ResponseEntity.ok(transcript);
    }
}

