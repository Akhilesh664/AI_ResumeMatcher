package com.example.AiResumeMatcher.services.internal;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SkillDictionary {

    // You can later move this to database / config file / YAML
    private final Set<String> skills = new HashSet<>(Arrays.asList(
            // Programming Languages
            "java", "python", "javascript", "typescript", "go", "rust", "c++", "c#", "php", "ruby", "swift", "kotlin",

            // Frameworks & Libraries
            "spring", "spring boot", "hibernate", "react", "angular", "vue", "node.js", "express", "django", "flask", "fastapi", "nestjs",

            // Databases & Tools
            "sql", "mysql", "postgresql", "mongodb", "oracle", "redis", "docker", "kubernetes", "jenkins", "git", "maven", "gradle",

            // Cloud & DevOps
            "aws", "azure", "gcp", "terraform", "ansible", "ci/cd", "devops",

            // Other common skills
            "rest", "rest api", "graphql", "microservices", "agile", "scrum", "tdd", "unit testing", "junit", "selenium",

            // 2025-2026 trending
            "machine learning", "ai", "llm", "prompt engineering", "langchain", "data science", "big data", "hadoop", "spark"
    ));

    // Optional: canonical name → variants
    private final Map<String, List<String>> variants = new HashMap<>();

    public SkillDictionary() {
        // Add common variations / misspellings / formats
        variants.put("java", Arrays.asList("java", "core java", "java se", "java ee"));
        variants.put("spring boot", Arrays.asList("spring boot", "springboot", "spring-boot"));
        variants.put("javascript", Arrays.asList("javascript", "js", "ecmascript", "es6", "es2015"));
        variants.put("react", Arrays.asList("react", "react.js", "reactjs"));
        // add more as you see in real resumes
    }

    /**
     * Extract skills from cleaned text (simple contains check + variant handling)
     */
    public List<String> extractSkills(String cleanedText) {
        List<String> found = new ArrayList<>();

        String lower = " " + cleanedText + " "; // padding to avoid partial matches

        for (String skill : skills) {
            if (lower.contains(" " + skill + " ")) {
                found.add(skill);
                continue;
            }

            // Check variants
            List<String> vars = variants.getOrDefault(skill, Collections.emptyList());
            for (String v : vars) {
                if (lower.contains(" " + v + " ")) {
                    found.add(skill); // canonical name
                    break;
                }
            }
        }

        return found;
    }

    /**
     * Count matched skills (for overlap score)
     */
    public double calculateSkillOverlap(List<String> resumeSkills, List<String> jdSkills) {
        if (jdSkills.isEmpty()) return 0.0;

        Set<String> jdSet = new HashSet<>(jdSkills);
        long matched = resumeSkills.stream().filter(jdSet::contains).count();

        return (double) matched / jdSkills.size();
    }
}