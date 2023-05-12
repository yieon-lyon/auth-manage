package com.yieon.practice.auth.lib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yieon.practice.auth.lib.model.constant.RegexPattern;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.specification.account.UserSpecificationsBuilder;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.util
 *     |AccountUtil.java
 * ------------------------
 * summary : 사용자 유틸리티
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class AccountUtil {
	public static Specification<User> generateUserSpecification(String searchParameters) {
		UserSpecificationsBuilder builder = new UserSpecificationsBuilder();
		Pattern pattern = Pattern.compile(RegexPattern.DEFAULT_PATTERN);
		Matcher matcher = pattern.matcher(searchParameters + ",");
		while (matcher.find()) {
			builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
		}
		return builder.build();
	}
}
