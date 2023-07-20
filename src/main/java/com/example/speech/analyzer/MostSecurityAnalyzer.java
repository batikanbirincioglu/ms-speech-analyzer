package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.speech.SpeechConstants.TOPIC_HOMELAND_SECURITY;

@Component
public class MostSecurityAnalyzer implements SpeechAnalyzer {
    @Override
    public String analyze(List<SpeechData> speechDataList) {
        Map<String, Integer> speakerToHomeLandSecurityTopicCount = speechDataList.stream()
                .filter(speechData -> speechData.getTopic().equals(TOPIC_HOMELAND_SECURITY))
                .collect(Collectors.groupingBy(SpeechData::getSpeaker))
                .entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue().size()));

        String mostSecurity = null;
        int mostSecurityCount = 0;
        int mostSecurityPersonCount = 0;
        for (Map.Entry<String, Integer> entry : speakerToHomeLandSecurityTopicCount.entrySet()) {
            if (mostSecurity == null) {
                mostSecurity = entry.getKey();
                mostSecurityCount = entry.getValue();
                mostSecurityPersonCount = 1;
            } else {
                if (entry.getValue() == mostSecurityCount) {
                    mostSecurityPersonCount++;
                } else if (entry.getValue() > mostSecurityCount) {
                    mostSecurity = entry.getKey();
                    mostSecurityCount = entry.getValue();
                    mostSecurityPersonCount = 1;
                }
            }
        }
        return mostSecurityPersonCount == 1 ? mostSecurity : null;
    }
}
