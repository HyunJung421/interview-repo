package com.portfolio.interview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.portfolio.interview.dto.InterviewDto;
import com.portfolio.interview.dto.PageDto;
import com.portfolio.interview.entity.Categories;
import com.portfolio.interview.entity.Interview;
import com.portfolio.interview.repository.CategoriesRepository;
import com.portfolio.interview.repository.InterviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {
    
    private final CategoriesRepository categoriesRepository;
    private final InterviewRepository interviewRepository;
    

    public InterviewDto.ListResponse getInterviewList(InterviewDto.ListRequest params) {

        Pageable pageable = PageRequest.of(params.pageInfo().currentPage() - 1, params.pageInfo().perPage());

        Page<InterviewDto.InterviewInfo> list = interviewRepository.findInterviewByConditions(params, pageable);

        PageDto.Response pageInfo = PageDto.Response.of(list.getNumber() + 1, list.getSize(), list.getTotalElements());

        InterviewDto.ListResponse result = InterviewDto.ListResponse.of(list.getContent(), pageInfo);

        return result;
    }

    public InterviewDto.AddResponse addInterview(InterviewDto.AddRequest params) {
        
        Categories categories = categoriesRepository.findByName(params.category());
        
        Interview interview = Interview.builder()
                                        .user(null)
                                        .categories(categories)
                                        .title(params.title())
                                        .content(params.content())
                                        .build();

        interviewRepository.save(interview);

        return null;
    }
}
