package com.yieon.practice.auth.lib.security;

import com.yieon.practice.auth.lib.api.AuthRestUrl;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.security
 *     |CustomAccessDecisionManager.java
 * ------------------------
 * summary : API 별 접근 허용 여부를 판단하는 AccessDecisionVoter
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class CustomAccessDecisionManager implements AccessDecisionVoter {

    private Map<String, List<String>> rules = new HashMap<>();
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class aClass) {
        return true;
    }

    /**
     * Request에 대한 접근 허용 여부 표결
     * - ACCESS_GRANTED : 자원 접근 허용
     * - ACCESS_ABSTAIN : 판단 보류 (다른 voter 인스턴스 결정에 맡김)
     * - ACCESS_DENIED  : 자원 접근 거부
     *
     * @param authentication 인증정보 (Authentication 인터페이스를 구현한 클래스의 인스턴스가 넘어온다.)
     * @param object		 현재 접근하고자 하는 자원 (FilterInvocation 객체)
     * @param collection	 현재 접근하는 자원(object)에 접근 가능한 권한목록
     * @return
     */
    @Override
    public int vote(Authentication authentication, Object object, Collection collection) {
        String uri = ((FilterInvocation) object).getRequestUrl();
        if (authentication instanceof OAuth2Authentication) {
            Set<String> scope = ((OAuth2Authentication) authentication).getOAuth2Request().getScope();

            int result = rules.keySet().stream().filter(key -> {
                Pattern pattern = Pattern.compile(key);
                Matcher matcher = pattern.matcher(uri);
                return matcher.find();
            }).filter(key -> {
                List<String> compare = rules.get(key).stream().map(privilege -> privilege.replace("PRIVILEGE_", "").toLowerCase()).collect(Collectors.toList());
                List<String> intersect = compare.stream().filter(scope::contains).collect(Collectors.toList());
                return intersect.size() != 0;
            }).findAny().map(s -> ACCESS_GRANTED).orElse(ACCESS_ABSTAIN);

            return result;
        } else if (checkWhiteList(uri)) {
            return ACCESS_GRANTED;
        } else {
            return ACCESS_DENIED;
        }
    }

    private boolean checkWhiteList(String uri) {
        boolean result = false;
		for (String pattern : AuthRestUrl.AUTH_WHITELIST) {
			if (pathMatcher.match(pattern, uri)) {
				result = true;
				break;
			}
		}
        return result;
    }
}