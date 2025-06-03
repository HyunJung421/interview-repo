package com.portfolio.interview.repository;

import com.portfolio.interview.dto.InterviewDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.Interview;
import com.portfolio.interview.repository.custom.CustomInterviewRepository;

import java.util.Optional;
import java.util.UUID;

public interface InterviewRepository extends JpaRepository<Interview, Long>, CustomInterviewRepository {

    Optional<Interview> findByKey(UUID key);
}
