package com.example.speech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechData {
    private String speaker;
    private String topic;
    private LocalDate date;
    private int wordCount;
}
