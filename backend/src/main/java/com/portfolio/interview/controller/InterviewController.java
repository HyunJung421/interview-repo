package com.portfolio.interview.controller;

import com.portfolio.interview.controller.docs.InterviewControllerDocs;
import com.portfolio.interview.dto.InterviewDto;
import com.portfolio.interview.service.InterviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewController implements InterviewControllerDocs {
    
    private final InterviewService interviewService;

    // 면접 질문 목록 조회
    @GetMapping("")
    public InterviewDto.ListResponse getInterviewList(@ModelAttribute InterviewDto.ListRequest request) {
        log.info("          :::::::::: Get interview Q&A list ::::::::::");

        return interviewService.getInterviewList(request);
    }

    // 면접 질문 상세 조회
    @GetMapping("/{key}")
    public InterviewDto.InterviewDetailResponse getInterviewDetail(@PathVariable("key") String key) {
        log.info("          :::::::::: Get interview Q&A detail ::::::::::");

        return interviewService.getInterviewDetail(key);
    }

    // 면접 질문 등록
    @PostMapping("/create")
    public InterviewDto.AddResponse addInterview(@RequestBody InterviewDto.AddRequest request) {
        log.info("          :::::::::: Add interview Q&A ::::::::::");

        return interviewService.addInterview(request);
    }
}
