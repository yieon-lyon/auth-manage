package com.yieon.practice.auth.web.rest.form;

import com.yieon.practice.auth.lib.model.constant.account.UserClient;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-06
 * <PRE>
 * ------------------------
 * summary : 회원가입 form
 * ------------------------
 * Revision history
 * 2023-04-06. yieon : Initial creation
 * </PRE>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    @ApiModelProperty(position = 1, required = true, value = "사용자 이메일")
    private String email;

    @ApiModelProperty(position = 2, required = true, value = "비밀번호")
    private String password;

    @ApiModelProperty(position = 3, required = true, value = "클라이언트 유형")
    private String clientType;

    @ApiModelProperty(position = 4, required = true, value = "활성화 그룹")
    private List<String> activateClients;

}
