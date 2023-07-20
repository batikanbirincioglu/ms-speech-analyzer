package com.example.speech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeechEvaluationResult {
    private String mostSpeeches;
    private String mostSecurity;
    private String leastWordy;
}
