package com.yieon.practice.auth.lib.model.dto.account;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.model.dto.account
 *     |RoleDto.java
 * ------------------------
 * summary : Role Dto
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	@ApiModelProperty(position = 1,	required = true, value = "권한그룹 코드")
	private String roleCode;

	@ApiModelProperty(position = 2,	required = true, value = "권한그룹명")
	private String roleName;

	@ApiModelProperty(position = 3, required = true, value = "권한그룹 내 권한 코드 목록")
	private List<String> assignedPrivileges;
}
