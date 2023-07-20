package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;

import java.util.List;

public interface SpeechAnalyzer {
    public String analyze(List<SpeechData> speechDataList);
}
