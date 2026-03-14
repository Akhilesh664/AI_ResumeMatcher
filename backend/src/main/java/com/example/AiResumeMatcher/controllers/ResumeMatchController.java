package com.example.AiResumeMatcher.controllers;

import com.example.AiResumeMatcher.dto.MatchResponseDTO;
import com.example.AiResumeMatcher.services.ResumeMatcherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/match")
@CrossOrigin("*")
public class ResumeMatchController {

    private final ResumeMatcherService matcherService;

    public ResumeMatchController(ResumeMatcherService matcherService) {
        this.matcherService = matcherService;
    }

    @PostMapping("/resume")
    public ResponseEntity<MatchResponseDTO> matchResume(
            @RequestPart("resume") MultipartFile resumeFile,
            @RequestPart("jobDescription") String jobDescription) {

        try{
            MatchResponseDTO response = matcherService.computeMatch(resumeFile, jobDescription);
            return ResponseEntity.ok(response);
        } catch (Exception e){
            e.printStackTrace(); // Log the stack trace for debugging
            return ResponseEntity.badRequest().body(new MatchResponseDTO(0, "Error" + e.getMessage(), null));
        }
    }
}
