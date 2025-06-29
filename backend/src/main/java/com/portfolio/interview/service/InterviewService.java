package com.portfolio.interview.service;

import com.portfolio.interview.dto.InterviewDto;
import com.portfolio.interview.dto.PageDto;
import com.portfolio.interview.entity.Categories;
import com.portfolio.interview.entity.Interview;
import com.portfolio.interview.repository.BookmarksRepository;
import com.portfolio.interview.repository.CategoriesRepository;
import com.portfolio.interview.repository.InterviewRepository;
import com.portfolio.interview.security.SecurityUserService;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final BookmarksRepository bookmarksRepository;
    private final CategoriesRepository categoriesRepository;
    private final InterviewRepository interviewRepository;
    private final SecurityUserService securityUserService;

    // 면접 질문 목록 조회
    public InterviewDto.ListResponse getInterviewList(InterviewDto.ListRequest params) {

        Pageable pageable = PageRequest.of(params.currentPage() - 1, params.perPage());

        Page<InterviewDto.InterviewInfo> list = interviewRepository.findInterviewByConditions(params, pageable);

        PageDto.Response pageInfo = PageDto.Response.of(list.getNumber() + 1, list.getSize(), list.getTotalElements());

        InterviewDto.ListResponse result = InterviewDto.ListResponse.of(list.getContent(), pageInfo);

        return result;
    }

    // 면접 질문 상세 조회
    public InterviewDto.InterviewDetailResponse getInterviewDetail(String key) {

        Interview interview = interviewRepository.findByKey(UUID.fromString(key)).orElseThrow(() -> new RestApiException(ResultCode.INTERVIEW_NOT_FOUND));

        Boolean bookmarked = bookmarksRepository.existsByInterviewSeq(interview.getSeq());

        InterviewDto.InterviewDetailResponse result = new InterviewDto.InterviewDetailResponse(interview.getKey().toString(), interview.getCategories().getName(), interview.getTitle(), interview.getContent(), bookmarked);

        return result;
    }
    
    // 면접 질문 등록
    public InterviewDto.AddResponse addInterview(InterviewDto.AddRequest params) {
        
        Categories categories = categoriesRepository.findByName(params.category());
        
        Interview interview = Interview.builder()
                                        .user(securityUserService.getUserDetails())
                                        .categories(categories)
                                        .title(params.title())
                                        .content(params.content())
                                        .build();

        interviewRepository.save(interview);

        return null;
    }
}
