package com.yieon.practice.auth.lib.repository.auth;

import com.yieon.practice.auth.lib.model.domain.auth.OAuthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.repository.auth
 *     |OAuthClientDetailsRepository.java
 * ------------------------
 * summary : OAuth2 사용자 상세 정보 Repository
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
@Repository
public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {

}
