package com.yieon.practice.auth.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-04
 * <PRE>
 * ------------------------
 * summary : InvalidPasswordException
 * ------------------------
 * Revision history
 * 2023-04-04. yieon : Initial creation
 * </PRE>
 */
public class InvalidPasswordException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
