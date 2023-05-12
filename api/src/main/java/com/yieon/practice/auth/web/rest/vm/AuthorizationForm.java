package com.yieon.practice.auth.web.rest.vm;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-08
 * <PRE>
 * ------------------------
 * summary : 인증 요청 FORM
 * ------------------------
 * Revision history
 * 2023-04-08. yieon : Initial creation
 * </PRE>
 */
@Data
public class AuthorizationForm {

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String clientId;

    private String authorization;

    private Boolean rememberMe;

}
