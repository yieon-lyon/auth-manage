package com.yieon.practice.auth.lib.repository.auth;

import com.yieon.practice.auth.lib.model.domain.auth.OAuthClientTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-08
 * <PRE>
 * com.yieon.practice.auth.lib.repository.auth
 *     |OAuthClientTokensRepository.java
 * ------------------------
 * summary : OAuth2 사용자 토큰 정보 Repository
 * ------------------------
 * Revision history
 * 2023. 04. 08. yieon : Initial creation
 * </PRE>
 */
@Repository
public interface OAuthClientTokensRepository extends JpaRepository<OAuthClientTokens, String> {

    OAuthClientTokens findByEmail(String email);
    OAuthClientTokens findByAccessToken(String accessToken);
    OAuthClientTokens findByRefreshToken(String refreshToken);

}
