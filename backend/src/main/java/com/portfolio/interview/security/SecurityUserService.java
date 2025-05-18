package com.portfolio.interview.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.portfolio.interview.entity.User;
import com.portfolio.interview.system.enums.ResultCode;
import com.portfolio.interview.system.exception.RestApiException;

@Service
public class SecurityUserService {
    
    private CustomUserDetails getPrincipal() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(userDetails == null) {
            throw new RestApiException(ResultCode.UNAUTHORIZED_USER);
        }

        return userDetails;
    }

    public User getUserDetails() {
        CustomUserDetails userDetails = getPrincipal();

        return userDetails.getUser();
    }

    public String getUserId() {
        CustomUserDetails userDetails = getPrincipal();

        return userDetails.getUserId();
    }

    public Long getUserSeq() {
        CustomUserDetails userDetails = getPrincipal();

        return userDetails.getUser().getSeq();
    }
}
