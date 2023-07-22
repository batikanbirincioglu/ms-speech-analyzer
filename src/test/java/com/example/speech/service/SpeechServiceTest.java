package com.example.speech.service;

import com.example.speech.model.SpeechData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class SpeechServiceTest {
    @InjectMocks
    private SpeechService speechService;

    @Test
    public void test_validateQueryParameters_invalid() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> speechService.validateQueryParameterNames(Map.of(
                "url1", "url1-value",
                "url2", "url2-value",
                "url4", "url4-value")));
        assertEquals("There is a misuse of query parameters, they should be like url1, url2, url3 etc...", exception.getMessage());
    }

    @Test
    public void test_validateQueryParameters_valid() {
        Assertions.assertDoesNotThrow(() -> speechService.validateQueryParameterNames(Map.of(
                "url1", "url1-value",
                "url2", "url2-value",
                "url3", "url3-value")));
    }

    @Test
    public void test_validateTokenCountInALine_invalid() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> speechService
                .validateTokenCountInALine("url1", List.of("john", "homeland security", "2023-11-11", "24", "93485asdf")));
        assertEquals("Line[[john, homeland security, 2023-11-11, 24, 93485asdf]] defined in url :: url1 doesn't have token count :: 4", exception.getMessage());
    }

    @Test
    public void test_validateTokenCountInALine_valid() {
        Assertions.assertDoesNotThrow(() -> speechService.validateTokenCountInALine("url1", List.of("john", "homeland security", "2023-11-11", "24")));
    }

    @Test
    public void test_validateDateInALine_invalid() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> speechService
                .validateDateInALine("url1", List.of("john", "homeland security", "2023-11-11laksjdf", "24")));
        assertEquals("Format of the date defined in Line[[john, homeland security, 2023-11-11laksjdf, 24]] located at url :: url1 is not correct", exception.getMessage());
    }

    @Test
    public void test_validateDateInALine_valid() {
        Assertions.assertDoesNotThrow(() -> speechService.validateDateInALine("url1", List.of("john", "homeland security", "2023-11-11", "24")));
    }

    @Test
    public void test_validateWordCountInALine_invalid() {
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> speechService
                .validateWordCountInALine("url1", List.of("john", "homeland security", "2023-11-11", "24lasdf")));
        assertEquals("Line[[john, homeland security, 2023-11-11, 24lasdf]] defined in url :: url1 doesn't have 'words' token that is numeric", exception.getMessage());
    }

    @Test
    public void test_validateWordCountInALine_valid() {
        Assertions.assertDoesNotThrow(() -> speechService.validateWordCountInALine("url1", List.of("john", "homeland security", "2023-11-11", "24")));
    }

    @Test
    public void test_getSpeechDataMap() {
        String url1 = "url1";
        List<List<String>> url1Content = List.of(
                List.of("speaker-1", "topic-1", "2023-11-11", "10"),
                List.of("speaker-1", "topic-2", "2023-11-11", "5"),
                List.of("speaker-5", "topic-1", "2023-11-11", "5"),
                List.of("speaker-3", "topic-3", "2023-11-11", "15"),
                List.of("speaker-7", "topic-1", "2023-11-11", "20"));

        String url2 = "url2";
        List<List<String>> url2Content = List.of(
                List.of("speaker-1", "topic-1", "2023-11-11", "25"),
                List.of("speaker-2", "topic-3", "2023-11-11", "40"),
                List.of("speaker-3", "topic-2", "2023-11-11", "20"),
                List.of("speaker-5", "topic-1", "2023-11-11", "25"));

        String url3 = "url3";
        List<List<String>> url3Content = List.of(
                List.of("speaker-7", "topic-1", "2023-11-11", "25"),
                List.of("speaker-7", "topic-1", "2023-11-11", "10"),
                List.of("speaker-7", "topic-2", "2023-11-11", "50"));

        String url4 = "url4";
        List<List<String>> url4Content = List.of(
                List.of("speaker-2", "topic-1", "2023-11-11", "5"),
                List.of("speaker-2", "topic-1", "2023-11-11", "5"));

        Map<String, List<SpeechData>> speechDataMap = speechService.getSpeechDataMap(Map.of(url1, url1Content, url2, url2Content, url3, url3Content, url4, url4Content));

        List<SpeechData> speechDataList = speechDataMap.get("url1");
        assertEquals(5, speechDataList.size());
        assertEquals(SpeechData.builder().speaker("speaker-1").topic("topic-1").date(LocalDate.of(2023, 11, 11)).wordCount(10).build(),
                speechDataList.get(0));
        assertEquals(SpeechData.builder().speaker("speaker-1").topic("topic-2").date(LocalDate.of(2023, 11, 11)).wordCount(5).build(),
                speechDataList.get(1));
        assertEquals(SpeechData.builder().speaker("speaker-5").topic("topic-1").date(LocalDate.of(2023, 11, 11)).wordCount(5).build(),
                speechDataList.get(2));
        assertEquals(SpeechData.builder().speaker("speaker-3").topic("topic-3").date(LocalDate.of(2023, 11, 11)).wordCount(15).build(),
                speechDataList.get(3));
        assertEquals(SpeechData.builder().speaker("speaker-7").topic("topic-1").date(LocalDate.of(2023, 11, 11)).wordCount(20).build(),
                speechDataList.get(4));

        assertEquals(4, speechDataMap.get("url2").size());
        assertEquals(3, speechDataMap.get("url3").size());
        assertEquals(2, speechDataMap.get("url4").size());
    }
}
