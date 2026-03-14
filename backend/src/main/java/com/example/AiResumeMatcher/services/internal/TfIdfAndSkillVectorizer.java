package com.example.AiResumeMatcher.services.internal;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TfIdfAndSkillVectorizer {

    // ────────────────────────────────────────────────
    // Inner class to hold the full-text TF-IDF vectors
    // ────────────────────────────────────────────────
    public static class TextVectors {
        public final double[] resumeVector;
        public final double[] jdVector;
        public final List<String> vocabulary;

        public TextVectors(double[] resumeVector, double[] jdVector, List<String> vocabulary) {
            this.resumeVector = resumeVector;
            this.jdVector = jdVector;
            this.vocabulary = vocabulary;
        }
    }

    /**
     * Computes standard TF-IDF vectors for the full cleaned texts
     * (this part stays almost identical to before)
     */
    public TextVectors computeTextVectors(String cleanResume, String cleanJd) {
        List<String> resumeTokens = Arrays.asList(cleanResume.split("\\s+"));
        List<String> jdTokens      = Arrays.asList(cleanJd.split("\\s+"));

        Set<String> vocabSet = new HashSet<>();
        vocabSet.addAll(resumeTokens);
        vocabSet.addAll(jdTokens);

        List<String> vocabulary = new ArrayList<>(vocabSet);
        vocabulary.sort(String::compareTo);

        int vocabSize = vocabulary.size();
        double[] resumeVec = new double[vocabSize];
        double[] jdVec     = new double[vocabSize];

        int N = 2; // two documents only

        for (int i = 0; i < vocabSize; i++) {
            String term = vocabulary.get(i);

            double tfR = (double) Collections.frequency(resumeTokens, term) / resumeTokens.size();
            double tfJ = (double) Collections.frequency(jdTokens, term) / jdTokens.size();

            int df = (Collections.frequency(resumeTokens, term) > 0 ? 1 : 0) +
                    (Collections.frequency(jdTokens, term) > 0 ? 1 : 0);

            // Slightly smoothed IDF
            double idf = Math.log((N + 1.0) / (df + 1.0)) + 1;

            resumeVec[i] = tfR * idf;
            jdVec[i]     = tfJ * idf;
        }

        return new TextVectors(resumeVec, jdVec, vocabulary);
    }

    /**
     * Simple skill overlap ratio (0.0 – 1.0)
     * → how many of the JD's skills are present in the resume
     */
    public double computeSkillOverlapRatio(
            List<String> resumeSkills,
            List<String> jdSkills) {

        if (jdSkills == null || jdSkills.isEmpty()) {
            return 0.0;
        }

        Set<String> jdSkillSet = new HashSet<>(jdSkills);
        long matchedCount = resumeSkills.stream()
                .filter(jdSkillSet::contains)
                .count();

        return (double) matchedCount / jdSkills.size();
    }

    /**
     * Optional: create a very small vector only from skills (for debugging or future use)
     */
    public double[] createSkillOnlyVector(List<String> skills, List<String> allVocab) {
        double[] vec = new double[allVocab.size()];
        Set<String> skillSet = new HashSet<>(skills);

        for (int i = 0; i < allVocab.size(); i++) {
            if (skillSet.contains(allVocab.get(i))) {
                vec[i] = 1.0; // binary presence (or you can use count)
            }
        }
        return vec;
    }
}