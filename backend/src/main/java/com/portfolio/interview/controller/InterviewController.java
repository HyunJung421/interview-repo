package com.portfolio.interview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.interview.dto.InterviewDto;
import com.portfolio.interview.service.InterviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewController {
    
    private final InterviewService interviewService;

    // 면접 질문 목록 조회
    @GetMapping("")
    public InterviewDto.ListResponse getInterviewList(@ModelAttribute InterviewDto.ListRequest request) {
        log.info("          :::::::::: Get interview Q&A list ::::::::::");

        return interviewService.getInterviewList(request);
    }
    
    // 면접 질문 등록
    @PostMapping("/create")
    public InterviewDto.AddResponse addInterview(@RequestBody InterviewDto.AddRequest request) {
        log.info("          :::::::::: Add interview Q&A ::::::::::");

        return interviewService.addInterview(request);
    }
}
