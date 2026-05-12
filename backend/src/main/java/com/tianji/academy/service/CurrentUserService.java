package com.tianji.academy.service;

import com.tianji.academy.exception.BusinessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public Long userId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            throw new BusinessException(401, "未登录");
        }
        Object details = authentication.getDetails();
        if (details instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(details.toString());
    }
}
