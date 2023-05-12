package com.yieon.practice.auth.web.rest.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-09-07
 * <PRE>
 * ------------------------
 * summary : 사용자 비밀번호 RESET FORM
 * ------------------------
 * Revision history
 * 2023-09-07. yieon : Initial creation
 * </PRE>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordForm {
    @ApiModelProperty(position = 1,required = true, value = "사용자 이메일")
    private String resetKey;

    @ApiModelProperty(position = 2,required = true, value = "새로운 비밀번호")
    private String newPassword;

}
