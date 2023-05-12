package com.yieon.practice.auth.lib.model.constant.account;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.lib.model.constant.account
 *     |UserClient.java
 * ------------------------
 * summary : 기본 사용자 Client 서버그룹
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
public enum UserClient {
	CLIENT_AUTH("CLIENT_AUTH", "AUTH", "auth"),
	public final String client;
	public final String description;
	public final String value;

	UserClient(String client, String description, String value) {
		this.client = client;
		this.description = description;
		this.value = value;
	}

	public static UserClient getInstance(String code) {
		for (UserClient userClient : UserClient.values()) {
			if (userClient.client.equals(code))
				return userClient;
		}

		throw new IllegalArgumentException("No such default user role.");
	}

}