package com.yieon.practice.auth.lib.model.dto.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.model.dto.account
 *     |PrivilegeDto.java
 * ------------------------
 * summary : PrivilegeDto
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeDto {

	@ApiModelProperty(
			position = 1,
			required = true,
			value = "권한 코드"
	)
	private String privilegeCode;

	@ApiModelProperty(
			position = 2,
			required = true,
			value = "권한명"
	)
	private String privilegeName;

	@ApiModelProperty(
			position = 3,
			required = true,
			value = "소속 권한그룹 코드 목록"
	)
	private List<String> assignedRoles;

}
