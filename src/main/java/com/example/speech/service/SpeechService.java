package com.example.speech.service;

import com.example.speech.SpeechConstants;
import com.example.speech.analyzer.LeastWordyAnalyzer;
import com.example.speech.analyzer.MostSecurityAnalyzer;
import com.example.speech.analyzer.MostSpeechesAnalyzer;
import com.example.speech.dto.SpeechEvaluationResult;
import com.example.speech.model.SpeechData;
import com.example.speech.utils.IoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpeechService {
    private final MostSpeechesAnalyzer mostSpeechesAnalyzer;
    private final MostSecurityAnalyzer mostSecurityAnalyzer;
    private final LeastWordyAnalyzer leastWordyAnalyzer;

    public SpeechEvaluationResult evaluate(Map<String, String> urlMap) {
        urlMap = validateQueryParameterNames(urlMap);

        Map<String, List<List<String>>> fileContents = urlMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,entry -> IoUtils.readCsvFromUrl(entry.getValue())));
        Map<String, List<SpeechData>> speechDataMap = getSpeechDataMap(fileContents);
        List<SpeechData> speechDataList = speechDataMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());

        String mostSpeeches = mostSpeechesAnalyzer.analyze(speechDataList);
        String mostSecurity = mostSecurityAnalyzer.analyze(speechDataList);
        String leastWordy = leastWordyAnalyzer.analyze(speechDataList);

        return new SpeechEvaluationResult(mostSpeeches, mostSecurity, leastWordy);
    }

    protected Map<String, String> validateQueryParameterNames(Map<String, String> urlMap) {
        List<String> queryParameterNames = urlMap.keySet().stream().sorted().collect(Collectors.toList());
        int size = queryParameterNames.size();
        for (int i = 0; i < size; i++) {
            if (!queryParameterNames.get(i).equals("url" + (i + 1))) {
                throw new RuntimeException("There is a misuse of query parameters, they should be like url1, url2, url3 etc...");
            }
        }
        return urlMap;
    }

    protected Map<String, List<SpeechData>> getSpeechDataMap(Map<String, List<List<String>>> fileContents) {
        return fileContents.entrySet().stream().map(entry -> {
            AbstractMap.SimpleEntry<String, List<SpeechData>> speechDataListEntry = new AbstractMap.SimpleEntry<>(entry.getKey(), new ArrayList<>());
            List<SpeechData> speechDataList = entry.getValue().stream().map(line -> {
                validateTokenCountInALine(entry.getKey(), line);
                String speaker = line.get(0);
                String topic = line.get(1);
                LocalDate date = validateDateInALine(entry.getKey(), line);
                int wordCount = validateWordCountInALine(entry.getKey(), line);
                return new SpeechData(speaker, topic, date, wordCount);
            }).collect(Collectors.toList());
            speechDataListEntry.setValue(speechDataList);
            return speechDataListEntry;
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    protected void validateTokenCountInALine(String url, List<String> line) {
        if (line.size() != SpeechConstants.TOKEN_NUMBER_FOR_A_LINE) {
            throw new RuntimeException(String.format("Line[%s] defined in url :: %s doesn't have token count :: %s", line, url, SpeechConstants.TOKEN_NUMBER_FOR_A_LINE));
        }
    }

    protected LocalDate validateDateInALine(String url, List<String> line) {
        try {
            return LocalDate.parse(line.get(2), DateTimeFormatter.ofPattern(SpeechConstants.SPEECH_DATE_FORMAT));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Format of the date defined in Line[%s] located at url :: %s is not correct", line, url));
        }
    }

    protected int validateWordCountInALine(String url, List<String> line) {
        try {
            return Integer.parseInt(line.get(3));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Line[%s] defined in url :: %s doesn't have 'words' token that is numeric", line, url));
        }
    }
}
