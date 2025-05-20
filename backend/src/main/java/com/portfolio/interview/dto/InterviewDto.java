package com.portfolio.interview.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class InterviewDto {
    
    // 면접 질문 조회 Request
    public record ListRequest(
        String title, 
        String category, 
        Boolean bookmarked,
        int currentPage,
        int perPage
    ){}

    // 면접 질문 조회 Response
    public record ListResponse(
        List<InterviewInfo> list,
        PageDto.Response pageInfo
    ){
        public static ListResponse of(List<InterviewInfo> list, PageDto.Response pageInfo) {
            return new ListResponse(list, pageInfo);
        }
    }

    // 면접 질문 조회 Response > list
    public record InterviewInfo(
        String key, 
        String category,
        String title,
        LocalDateTime updatedAt
    ){
        public InterviewInfo(UUID key, String category, String title, LocalDateTime updatedAt) {
            this(key.toString(), category, title, updatedAt);
        }    
    }

    // 면접 질문 등록 Request
    public record AddRequest(
        String category, 
        String title, 
        String content){}

    // 면접 질문 등록 Response
    public record AddResponse(String key){}

}
