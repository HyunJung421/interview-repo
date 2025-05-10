package com.portfolio.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.interview.entity.Interview;
import com.portfolio.interview.repository.custom.CustomInterviewRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long>, CustomInterviewRepository {
    
}
