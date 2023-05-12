package com.yieon.practice.auth.lib.exception;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.exception
 *     |FileStorageException.java
 * ------------------------
 * summary : FileStorageException
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class FileStorageException extends RuntimeException {

	private static final long serialVersionUID = 4187708117406909743L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
