package com.example.AiResumeMatcher.repositories;

import com.example.AiResumeMatcher.models.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchResultRepository extends JpaRepository<MatchResult, Long> {

}
