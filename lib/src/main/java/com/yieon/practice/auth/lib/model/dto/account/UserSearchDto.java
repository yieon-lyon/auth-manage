package com.yieon.practice.auth.lib.model.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.model.dto.account
 *     |UserSearchDto.java
 * ------------------------
 * summary : UserSearch Dto
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@ApiModel
@Data
public class UserSearchDto {

	@ApiModelProperty(position = 1,required = false, value = "사용자 이메일")
	private String email;

	@ApiModelProperty(position = 2,required = false, value = "사용자 이름")
	private String name;

	@ApiModelProperty(position = 3,required = false, value = "권한그룹 코드")
	private String roleCode;

}
