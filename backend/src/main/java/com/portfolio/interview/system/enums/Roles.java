package com.portfolio.interview.system.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {
    ADMIN(1L), // 관리자
    USER(2L); // 일반 사용자

    private final Long seq;
}