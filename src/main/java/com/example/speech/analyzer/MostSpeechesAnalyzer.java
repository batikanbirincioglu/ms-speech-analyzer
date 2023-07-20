package com.example.speech.analyzer;

import com.example.speech.model.SpeechData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.speech.SpeechConstants.YEAR_2013;

@Component
public class MostSpeechesAnalyzer implements SpeechAnalyzer {
    @Override
    public String analyze(List<SpeechData> speechDataList) {
        Map<String, Integer> speaker2MostSpeechesMap = new HashMap<>();
        for (SpeechData speechData : speechDataList) {
            Integer mostSpeechCount = speaker2MostSpeechesMap.getOrDefault(speechData.getSpeaker(), 0);
            if (speechData.getDate().getYear() == YEAR_2013) {
                speaker2MostSpeechesMap.put(speechData.getSpeaker(), mostSpeechCount + 1);
            }
        }

        String mostSpeeches = null;
        int mostSpeechesCount = 0;
        int mostSpeechesPersonCount = 0;
        for (Map.Entry<String, Integer> entry : speaker2MostSpeechesMap.entrySet()) {
            if (entry.getKey() == null) {
                mostSpeeches = entry.getKey();
                mostSpeechesCount = entry.getValue();
                mostSpeechesPersonCount = 1;
            } else {
                if (entry.getValue() == mostSpeechesCount) {
                    mostSpeechesPersonCount++;
                } else if (entry.getValue() > mostSpeechesCount) {
                    mostSpeeches = entry.getKey();
                    mostSpeechesCount = entry.getValue();
                    mostSpeechesPersonCount = 1;
                }
            }
        }
        return mostSpeechesPersonCount == 1 ? mostSpeeches : null;
    }
}
