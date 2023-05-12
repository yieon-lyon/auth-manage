package com.yieon.practice.auth.web.rest.errors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-04
 * <PRE>
 * ------------------------
 * summary : EmailAlreadyUsedException
 * ------------------------
 * Revision history
 * 2023-04-04. yieon : Initial creation
 * </PRE>
 */
public class EmailAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email is already in use!", "userManagement", "emailexists");
    }
}
