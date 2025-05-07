package com.portfolio.interview.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.portfolio.interview.dto.InterviewDto;

public interface CustomInterviewRepository {
    Page<InterviewDto.InterviewInfo> findInterviewByConditions(InterviewDto.ListRequest conditions, Pageable pageable);
}
