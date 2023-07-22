package com.example.speech.utils;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IoUtils {
    public static List<List<String>> readCsvFromUrl(String url) {
        List<List<String>> fileContent;
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(getUrlConnection(url).getInputStream()))) {
            fileContent = csvReader.readAll().stream().map(line -> Arrays.stream(line).collect(Collectors.toList())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Exception occurred while reading csv file from url : %s", url), e);
        }
        return fileContent;
    }

    protected static URLConnection getUrlConnection(String url) {
        try {
            URL urlObject = new URL(url);
            URLConnection urlConnection = urlObject.openConnection();
            return urlConnection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
