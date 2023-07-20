package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LeastWordyAnalyzer implements SpeechAnalyzer {
    @Override
    public String analyze(List<SpeechData> speechDataList) {
        Map<String, Integer> speaker2TotalWordCountMap = speechDataList.stream()
                .collect(Collectors.groupingBy(SpeechData::getSpeaker, Collectors.reducing(new SpeechData(), (data1, data2) -> {
                    SpeechData speechData = new SpeechData();
                    speechData.setWordCount(data1.getWordCount() + data2.getWordCount());
                    return speechData;
                })))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getWordCount()));

        String leastWordy = null;
        int leastWordyWordCount = 0;
        int leastWordyPersonCount = 0;
        for (Map.Entry<String, Integer> entry : speaker2TotalWordCountMap.entrySet()) {
            if (leastWordy == null) {
                leastWordy = entry.getKey();
                leastWordyPersonCount = 1;
                leastWordyWordCount = entry.getValue();
            } else {
                if (entry.getValue() == leastWordyWordCount) {
                    leastWordyPersonCount++;
                } else if (entry.getValue() < leastWordyWordCount) {
                    leastWordy = entry.getKey();
                    leastWordyWordCount = entry.getValue();
                    leastWordyPersonCount = 1;
                }
            }
        }
        return leastWordyPersonCount == 1 ? leastWordy : null;
    }
}
