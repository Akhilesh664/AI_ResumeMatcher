package com.example.AiResumeMatcher.services;

import com.example.AiResumeMatcher.dto.MatchResponseDTO;
import com.example.AiResumeMatcher.models.MatchResult;
import com.example.AiResumeMatcher.repositories.MatchResultRepository;
import com.example.AiResumeMatcher.services.internal.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class ResumeMatcherService {

    private final PdfExtractor pdfExtractor;
    private final TextPreprocessor textPreprocessor;
    private final TfIdfAndSkillVectorizer tfIdfAndSkillVectorizer;
    private final CosineSimilarityCalculator cosineCalculator;
    private final SkillDictionary skillDictionary;
    private final MatchResultRepository matchResultRepository;

    public ResumeMatcherService(PdfExtractor pdfExtractor
            , TextPreprocessor textPreprocessor
            , TfIdfAndSkillVectorizer tfIdfAndSkillVectorizer
            , CosineSimilarityCalculator cosineCalculator
            , SkillDictionary skillDictionary
            , MatchResultRepository matchResultRepository) {
        this.pdfExtractor = pdfExtractor;
        this.textPreprocessor = textPreprocessor;
        this.tfIdfAndSkillVectorizer = tfIdfAndSkillVectorizer;
        this.cosineCalculator = cosineCalculator;
        this.skillDictionary = skillDictionary;
        this.matchResultRepository = matchResultRepository;
    }

    public MatchResponseDTO computeMatch(MultipartFile resumeFile, String jobDescription) throws Exception {

        // 1. Extract text
        String rawResume = pdfExtractor.extractTextFromPdf(resumeFile);

        // 2. Preprocess both
        String cleanResume = textPreprocessor.preprocess(rawResume);
        String cleanJd = textPreprocessor.preprocess(jobDescription);

        if (cleanResume.isEmpty() || cleanJd.isEmpty()) {
            return new MatchResponseDTO(0, "Weak", new ArrayList<>());
        }

        List<String> resumeSkills = skillDictionary.extractSkills(cleanResume);
        List<String> jdSkills     = skillDictionary.extractSkills(cleanJd);

        // 3. Vectorize
        TfIdfAndSkillVectorizer.TextVectors vectors =
                tfIdfAndSkillVectorizer.computeTextVectors(cleanResume, cleanJd);

        double cosineSim = cosineCalculator.cosine(vectors.resumeVector, vectors.jdVector);
        double textScore = cosineSim * 100.0;   // 0–100

        // 2. Skill overlap score
        double skillRatio = skillDictionary.calculateSkillOverlap(resumeSkills, jdSkills);
        double skillScore = skillRatio * 100.0;

        // 3. Hybrid final score (recommended starting weights)
        double finalScore = (textScore * 0.55) + (skillScore * 0.45);


        // 5. Level
        String level = finalScore >= 75 ? "Strong" :
                finalScore >= 45 ? "Medium" : "Weak";

        // 6. Common keywords
        Set<String> resumeWords = new HashSet<>(Arrays.asList(cleanResume.split("\\s+")));
        Set<String> jdWords = new HashSet<>(Arrays.asList(cleanJd.split("\\s+")));
        resumeWords.retainAll(jdWords);
        List<String> common = new ArrayList<>(resumeWords);
        common.sort(String::compareTo);

        // Optional: save result
        MatchResult result = new MatchResult();
        result.setResumeFileName(resumeFile.getOriginalFilename());
        result.setJobDescriptionSnippet(jobDescription.substring(0, Math.min(100, jobDescription.length())));
        result.setMatchScore(finalScore);
        result.setMatchLevel(level);
        matchResultRepository.save(result);

        return new MatchResponseDTO(finalScore, level, common);
    }
}
