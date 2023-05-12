package com.yieon.practice.auth.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023. 04. 29
 * <PRE>
 * ------------------------
 * summary : 인증세션으로 부터 사용자 관련 정보를 취득하기 위한 Util 클래스
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
@SuppressWarnings("unchecked")
public final class SecurityContextUtils implements Serializable {

    public SecurityContextUtils() {
    }

    public static boolean isLoggedIn() {
        Authentication authentication = authentication();
        if (authentication == null)
            return false;
        return authentication.isAuthenticated();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = authentication();
        if (authentication == null)
            return false;

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return authentication.getAuthorities().contains(grantedAuthority);
    }

    public static String getUsername() {
        Authentication authentication = authentication();
        if (authentication == null)
            return StringUtils.EMPTY;

        return (String) authentication.getPrincipal();
    }

    public static List<String> getRoles() {
        Authentication authentication = authentication();
        if (authentication == null)
            return new ArrayList<>();
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        List<String> roles = authorities.stream().map(item -> item.getAuthority()).collect(Collectors.toList());
        return roles;
    }

    public static String getAccessToken() {
        Authentication authentication = authentication();
        if (authentication == null)
            return StringUtils.EMPTY;

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return details.getTokenValue();
//        Map<String, String> details = (Map<String, String>) authentication.getDetails();
//        return details.get("access_token");
    }

    public static Authentication authentication() {
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext==null) {
            throw new SecurityContextNotFoundException("SecurityContext Not Found Exception");
        }

        final Authentication authentication = securityContext.getAuthentication();
        if(authentication==null) {
            throw new SecurityContextNotFoundException("SecurityContext Authentication Not Found Exception");
        }
        return authentication;
    }

}
