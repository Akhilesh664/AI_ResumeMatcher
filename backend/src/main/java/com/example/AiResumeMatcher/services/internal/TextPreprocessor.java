package com.example.AiResumeMatcher.services.internal;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TextPreprocessor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "a", "an", "the", "and", "or", "but", "is", "are", "was", "were", "be", "being", "been",
            "have", "has", "had", "do", "does", "did", "will", "would", "shall", "should", "can", "could",
            "may", "might", "must", "i", "you", "he", "she", "it", "we", "they", "me", "him", "her", "us", "them"
            // add more if needed
    ));

    public String preprocess(String text){
        if(text == null || text.isEmpty()){
            return "";
        }

        text = text.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();

        return Arrays.stream(text.split("\\s+"))
                .filter(word -> !STOP_WORDS.contains(word) && word.length() > 1)
                .collect(Collectors.joining(" "));

    }



}
