package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class MostSpeechesAnalyzerTest {
    @InjectMocks
    private MostSpeechesAnalyzer mostSpeechesAnalyzer;

    @Test
    public void test_analyze_uniqueExist() {
        LocalDate now = LocalDate.now();
        LocalDate year2013 = now.withYear(2013);

        List<SpeechData> speechDataList = new ArrayList<>();
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("topic-1").date(year2013).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-2").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("topic-5").date(year2013).wordCount(15).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("topic-11").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-3").topic("topic-3").date(year2013).wordCount(33).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-22").date(now).wordCount(2).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-4").topic("topic-3").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("topic-5").date(year2013).wordCount(66).build());

        assertEquals("speaker-5", mostSpeechesAnalyzer.analyze(speechDataList));
    }

    @Test
    public void test_analyze_uniqueNotExist() {
        LocalDate now = LocalDate.now();
        LocalDate year2013 = now.withYear(2013);

        List<SpeechData> speechDataList = new ArrayList<>();
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("topic-1").date(year2013).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-2").date(now).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("topic-5").date(year2013).wordCount(15).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-1").topic("topic-11").date(year2013).wordCount(3).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-3").topic("topic-3").date(year2013).wordCount(33).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-2").topic("topic-22").date(now).wordCount(2).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-4").topic("topic-3").date(now).wordCount(5).build());
        speechDataList.add(SpeechData.builder().speaker("speaker-5").topic("topic-5").date(year2013).wordCount(66).build());

        assertEquals(null, mostSpeechesAnalyzer.analyze(speechDataList));
    }
}
