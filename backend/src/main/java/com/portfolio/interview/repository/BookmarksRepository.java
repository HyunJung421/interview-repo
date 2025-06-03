package com.portfolio.interview.repository;

import com.portfolio.interview.entity.Bookmarks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarksRepository extends JpaRepository<Bookmarks, Long> {
    Boolean existsByInterviewSeq(Long seq);
}
