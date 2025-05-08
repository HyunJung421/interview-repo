package com.portfolio.interview.repository.custom;

import static com.portfolio.interview.entity.QInterview.interview;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import com.portfolio.interview.dto.InterviewDto;
import com.portfolio.interview.entity.Interview;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomInterviewRepositoryImpl extends QuerydslRepositorySupport implements CustomInterviewRepository {

    public CustomInterviewRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Interview.class);
        this.queryFactory = queryFactory;
    }
    
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<InterviewDto.InterviewInfo> findInterviewByConditions(InterviewDto.ListRequest conditions, Pageable pageable) {
        List<InterviewDto.InterviewInfo> list = queryFactory.select(Projections.constructor(
                                                    InterviewDto.InterviewInfo.class,
                                                    interview.key,
                                                    interview.categories.name,
                                                    interview.title,
                                                    interview.updatedAt
                                                ))
                                                .from(interview)
                                                .where(titleContains(conditions.title()), categoryEquals(conditions.category()))
                                                .orderBy(interview.updatedAt.desc())
                                                .fetch();

        long total = queryFactory.select(interview.count())
                    .from(interview)
                    .where(titleContains(conditions.title()), categoryEquals(conditions.category()))
                    .fetchOne();

        return new PageImpl<>(list, pageable, total);
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? interview.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression categoryEquals(String category) {
        return StringUtils.hasText(category) ? interview.categories.name.eq(category) : null;
    }
    
}
