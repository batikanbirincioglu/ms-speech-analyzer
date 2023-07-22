package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MostSecurityAnalyzerTest {
    @InjectMocks
    private MostSecurityAnalyzer mostSecurityAnalyzer;

    @Test
    public void test_analyze_uniqueExist() {
        LocalDate now = LocalDate.now();

        List<SpeechData> speechDataList = new ArrayList<>();
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("homeland security").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-2").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("homeland security").date(now).wordCount(15).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("topic-11").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-3").topic("topic-3").date(now).wordCount(33).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("homeland security").date(now).wordCount(2).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-4").topic("topic-3").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("homeland security").date(now).wordCount(66).build());

        assertEquals("speaker-5", mostSecurityAnalyzer.analyze(speechDataList));
    }

    @Test
    public void test_analyze_uniqueNotExist() {
        LocalDate now = LocalDate.now();

        List<SpeechData> speechDataList = new ArrayList<>();
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("homeland security").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-2").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("homeland security").date(now).wordCount(15).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("homeland security").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-3").topic("topic-3").date(now).wordCount(33).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("homeland security").date(now).wordCount(2).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-4").topic("topic-3").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("homeland security").date(now).wordCount(66).build());

        assertEquals(null, mostSecurityAnalyzer.analyze(speechDataList));
    }
}
