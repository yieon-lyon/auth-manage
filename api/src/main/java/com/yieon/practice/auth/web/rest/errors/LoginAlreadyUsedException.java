package com.yieon.practice.auth.web.rest.errors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-04
 * <PRE>
 * ------------------------
 * summary : LoginAlreadyUsedException
 * ------------------------
 * Revision history
 * 2023-04-04. yieon : Initial creation
 * </PRE>
 */
public class LoginAlreadyUsedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LoginAlreadyUsedException() {
        super(ErrorConstants.LOGIN_ALREADY_USED_TYPE, "Login name already used!", "userManagement", "userexists");
    }
}
