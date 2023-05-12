package com.yieon.practice.auth.lib.error;

import lombok.Getter;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.error
 *     |ErrorCode.java
 * ------------------------
 * summary : 에러 관련 열거형 상수
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@Getter
public enum ErrorCode {
	BAD_REQUEST("ERR_400", "error.code.message.400", 400),
	FORBIDDEN("ERR_403", "error.code.message.403", 403),
	RESOURCE_NOT_FOUND("ERR_404", "error.code.message.404", 404),
	METHOD_NOT_ALLOWED("ERR_405", "error.code.message.405", 405),
	NOT_ACCEPTABLE("ERR_406", "error.code.message.406", 406),
	UNSUPPORTED_MEDIA_TYPE("ERR_415", "error.code.message.415", 415),
	INTERNAL_SERVER_ERROR("ERR_500", "error.code.message.500", 500),
	SERVICE_UNAVAILABLE("ERR_503", "error.code.message.503", 503);

	private final String code;
	private final String message;
	private final int status;

	ErrorCode(String code, String message, int status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
