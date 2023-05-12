package com.yieon.practice.auth.lib.model.dto.account;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.model.dto.account
 *     |UserDto.java
 * ------------------------
 * summary : User Dto
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@ApiModel
@Data
public class UserDto {
	
	@ApiModelProperty(position = 1,	required = true, value = "이메일")
	private String email;

//	@ApiModelProperty(position = 2,	required = true, value = "이름")
//	private String name;
	
	@ApiModelProperty(position = 3,	required = true, value = "비밀번호")
	private String password;
	
//	@ApiModelProperty(position = 4,	required = true, value = "전화번호")
//	private String tellNo;

//	@ApiModelProperty(position = 5,	value = "주소")
//	private String address;
	
//	@ApiModelProperty(position = 6,	value = "생일")
//	private String birthDt;

	@ApiModelProperty(position = 7, required = true, value = "사용자 활성 여부")
	private Boolean activated;

	@ApiModelProperty(position = 8, required = true, value = "탈퇴여부")
	private Boolean disabled;

	@ApiModelProperty(position = 9,	required = true, value = "생성일자")
	private Instant createdDt;

	@ApiModelProperty(position = 10, value = "마지막 로그인 성공 일자")
	private Instant lstLoginDt;

	@ApiModelProperty(position = 10, value = "마지막 비밀번호 변경 일자")
	private Instant pwChangedDt;
	
	@ApiModelProperty(position = 12,	value = "권한 그룹")
	private List<String> assignedRoles;

	@ApiModelProperty(position = 13,	value = "클라이언트 그룹")
	private List<String> assignedClients;

	@ApiModelProperty(position = 13,	value = "클라이언트 사용여부 그룹")
	private Map<String, Boolean> assignedUserActivations;

	@Builder
	public UserDto(String email, String password, Boolean activated, Boolean disabled, Instant createdDt, Instant lstLoginDt, Instant pwChangedDt, List<String> assignedRoles, List<String> assignedClients, Map<String, Boolean> assignedUserActivations) {
		this.email = email;
		this.password = password;
		this.activated = activated;
		this.disabled = disabled;
		this.createdDt = createdDt;
		this.lstLoginDt = lstLoginDt;
		this.pwChangedDt = pwChangedDt;
		this.assignedRoles = assignedRoles;
		this.assignedClients = assignedClients;
	}
}