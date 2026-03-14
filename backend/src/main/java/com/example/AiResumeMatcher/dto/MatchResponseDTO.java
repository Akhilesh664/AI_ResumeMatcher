package com.example.AiResumeMatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MatchResponseDTO {
    private double matchScore;          // 0–100
    private String matchLevel;          // "Strong", "Medium", "Weak"
    private List<String> commonKeywords;
}