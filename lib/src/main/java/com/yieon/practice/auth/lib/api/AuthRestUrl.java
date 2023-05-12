package com.yieon.practice.auth.lib.api;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.api
 *     |AuthRestUrl.java
 * ------------------------
 * summary : OAuth2.0 API URL 관리자
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class AuthRestUrl {

	public static final String BASE_URL			= "/api/web";
	public static final String AUTHORITY		= "/auth";
	public static final String ACCOUNT			= "/account";
	public static final String MANUFACTURE		= "/manufacture";
	
	public static final String ORDER			= "/order";
	public static final String MANAGEMENT		= "/manage";
	
	public static final String AUTH_MANAGE_SERVICE		= BASE_URL + AUTHORITY + MANAGEMENT;
	
	public static final String ACCOUNT_MANUFACTURE_ORDER	= BASE_URL + ACCOUNT + MANUFACTURE + ORDER;

	public static final String[] AUTH_WHITELIST = {
			"/v2/api-docs",
			"/swagger-resources",
			"/swagger-resources/**",
			"/configuration/ui",
			"/configuration/security",
			"/swagger-ui.html",
			"/webjars/**",
			"/error",
			"/error/**"
	};
}
