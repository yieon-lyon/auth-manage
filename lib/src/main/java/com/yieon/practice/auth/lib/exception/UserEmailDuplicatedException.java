package com.yieon.practice.auth.lib.exception;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.exception
 *     |UserEmailDuplicatedException.java
 * ------------------------
 * summary : UserEmailDuplicatedException
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class UserEmailDuplicatedException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UserEmailDuplicatedException(){}
	
	public UserEmailDuplicatedException(String message){
		super(message);
	}
	
	public UserEmailDuplicatedException(Throwable cause){
		super(cause);
	}
	
	public UserEmailDuplicatedException(String message, Throwable cause){
		super(message, cause);
	}
	
	public UserEmailDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
