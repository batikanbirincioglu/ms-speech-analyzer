package com.example.speech.controller;

import com.example.speech.dto.SpeechEvaluationResult;
import com.example.speech.service.SpeechService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SpeechController {
    private final SpeechService speechService;

    @GetMapping("/evaluation")
    public SpeechEvaluationResult evaluate(@RequestParam Map<String, String> urls) {
        return speechService.evaluate(urls);
    }

    @GetMapping("/csv/1")
    public ResponseEntity<byte[]> getCsv1() throws UnsupportedEncodingException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/vnd.ms-excel");
        responseHeaders.add("Content-Disposition", "attachment; filename=csv1.csv");

        String data = "batikan,topic-1,2022-10-10,5\n" +
                "mert,topic-2,2023-05-30,10\n" +
                "jack,topic-2,2013-05-30,22\n" +
                "batikan,topic-3,2013-09-10,6\n" +
                "batikan,topic-3,2013-09-10,3\n" +
                "batikan,homeland security,2013-09-10,3\n" +
                "mert,homeland security,2013-09-10,3\n" +
                "batikan,homeland security,2013-09-10,3\n" +
                "mert,topic-3,2018-09-10,2";
        return new ResponseEntity<>(data.getBytes("ISO8859-15"), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/csv/2")
    public ResponseEntity<byte[]> getCsv2() throws UnsupportedEncodingException {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/vnd.ms-excel");
        responseHeaders.add("Content-Disposition", "attachment; filename=csv2.csv");

        String data = "john,topic-5,2022-06-10,1";
        return new ResponseEntity<>(data.getBytes("ISO8859-15"), responseHeaders, HttpStatus.OK);
    }
}
