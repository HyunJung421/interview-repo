package com.portfolio.interview.entity;

import com.querydsl.core.types.FactoryExpression;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_seq", nullable = false)
    private Interview interview;
}
