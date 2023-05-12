package com.yieon.practice.auth.web.rest.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-11
 * <PRE>
 * ------------------------
 * summary : 클라이언트 사용여부 변경 FORM
 * ------------------------
 * Revision history
 * 2023-04-11. yieon : Initial creation
 * </PRE>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientActivationForm {

    @ApiModelProperty(position = 1,required = true, value = "클라이언트 ID")
    private String clientInfo;

    @ApiModelProperty(position = 2,required = true, value = "활성화 여부")
    private Boolean activated;

}
