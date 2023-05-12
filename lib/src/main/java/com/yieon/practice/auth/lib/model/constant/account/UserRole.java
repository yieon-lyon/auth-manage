package com.yieon.practice.auth.lib.model.constant.account;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.lib.model.constant.account
 *     |UserRole.java
 * ------------------------
 * summary : 기본 사용자 권한그룹
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
public enum UserRole {
	ROLE_TRUSTED("ROLE_TRUSTED", "시스템 서버"),
	ROLE_SYSADMIN("ROLE_SYSADMIN", "시스템 관리자"),
	ROLE_ADMIN("ROLE_ADMIN", "관리자"),
	ROLE_USER("ROLE_USER", "사용자"),
	ROLE_GUEST("ROLE_GUEST", "게스트"),
	ROLE_ANONYMOUSE("ROLE_ANONYMOUSE", "익명 사용자");

	public final String role;
	public final String description;

	UserRole(String role, String description) {
		this.role = role;
		this.description = description;
	}

	public static UserRole getInstance(String code) {
		for (UserRole userRole : UserRole.values()) {
			if (userRole.role.equals(code))
				return userRole;
		}

		throw new IllegalArgumentException("No such default user role.");
	}

}