package com.yieon.practice.auth.config;

import com.yieon.practice.auth.lib.model.ClientUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * com.yieon.practice.auth.service
 *     |OAuthClientDetailsService.java
 * ------------------------
 * summary : JWT 토큰 Payload 값 커스터마이징
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		if (authentication.getPrincipal() instanceof ClientUserDetails) {
			ClientUserDetails details = (ClientUserDetails) authentication.getPrincipal();
			((DefaultOAuth2AccessToken) accessToken).setScope(details.getScopes());
		}

		return accessToken;
	}

}
