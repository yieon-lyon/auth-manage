package com.yieon.practice.auth.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : 인증세션 관련 예외
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
public class SecurityContextNotFoundException extends AuthenticationException {

    public SecurityContextNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    SecurityContextNotFoundException(String msg) {
        super(msg);
    }
}
