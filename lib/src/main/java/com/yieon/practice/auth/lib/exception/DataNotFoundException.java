package com.yieon.practice.auth.lib.exception;

import org.springframework.validation.Errors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.exception
 *     |DataNotFoundException.java
 * ------------------------
 * summary : DataNotFoundException
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class DataNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	private final String value;
	
	private Errors errors;
	
	public DataNotFoundException( String value) {
		this.value = value;
	}
	
	public Errors getErrors(){ 
		return errors; 
	}
	
	
	public String getValue(){
		return value;
	}
	
}
