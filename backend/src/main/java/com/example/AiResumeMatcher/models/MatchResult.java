package com.example.AiResumeMatcher.models;

import com.sun.source.doctree.EscapeTree;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resumeFileName;
    private String jobDescriptionSnippet;

    private double matchScore;
    private String matchLevel;

    private LocalDateTime createdAt = LocalDateTime.now();

}
