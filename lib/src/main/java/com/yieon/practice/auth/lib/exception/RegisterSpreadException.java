package com.yieon.practice.auth.lib.exception;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-06
 * <PRE>
 * ------------------------
 * summary : RegisterSpreadException
 * ------------------------
 * Revision history
 * 2023-04-06. yieon : Initial creation
 * </PRE>
 */
public class RegisterSpreadException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegisterSpreadException() {}

    public RegisterSpreadException(String message){
        super(message);
    }

    public RegisterSpreadException(String message, Throwable cause){
        super(message, cause);
    }
}
