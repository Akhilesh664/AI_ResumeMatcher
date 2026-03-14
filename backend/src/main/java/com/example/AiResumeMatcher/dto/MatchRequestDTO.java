package com.example.AiResumeMatcher.dto;

import lombok.Data;

@Data
public class MatchRequestDTO {

    private String jobDescription;
    // file is sent separately by multipart/form-data
}
