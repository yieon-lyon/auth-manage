package com.yieon.practice.auth.lib.exception;

import org.springframework.validation.Errors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-09-07
 * <PRE>
 * com.yieon.practice.auth.lib.exception
 *     |EmailNotFoundException.java
 * ------------------------
 * summary : EmailNotFoundException
 * ------------------------
 * Revision history
 * 2023. 09. 07. yieon : Initial creation
 * </PRE>
 */
public class EmailNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private final String value;

	private Errors errors;

	public EmailNotFoundException(String value) {
		this.value = value;
	}
	
	public Errors getErrors(){ 
		return errors; 
	}

	public String getValue(){
		return value;
	}
	
}
