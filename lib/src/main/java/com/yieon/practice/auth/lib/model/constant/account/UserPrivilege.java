package com.yieon.practice.auth.lib.model.constant.account;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.lib.model.constant.account
 *     |UserPrivilege.java
 * ------------------------
 * summary : 사용자 권한 열거형 상수
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
public enum UserPrivilege {

	PRIVILEGE_CREATE("PRIVILEGE_CREATE", "생성 권한"),
	PRIVILEGE_READ("PRIVILEGE_READ", "읽기 권한"),
	PRIVILEGE_UPDATE("PRIVILEGE_UPDATE", "수정 권한"),
	PRIVILEGE_DELETE("PRIVILEGE_DELETE", "삭제 권한");

	public final String privilege;
	public final String description;

	UserPrivilege(String privilege, String description) {
		this.privilege = privilege;
		this.description = description;
	}

	public String scope() {
		return privilege.replace("PRIVILEGE_", "").toLowerCase();
	}

	public static UserPrivilege getInstance(String privilege) {
		for (UserPrivilege userPrivilege : UserPrivilege.values()) {
			if (userPrivilege.privilege.equals(privilege)) {
				return userPrivilege;
			}
		}

		throw new IllegalArgumentException("No such user privilege.");
	}

}
