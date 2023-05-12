package com.yieon.practice.auth.lib.model.domain.auth;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 *  com.yieon.practice.auth.model.domain.auth
 *  |OAuthClientDetails.java
 * ------------------------
 * summary : OAuth2.0 Client 계정 정보
 *           - 고유 기본 필드명을 가지고 있으므로 필드명 변경 불가
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Entity
@Data
@NoArgsConstructor
public class OAuthClientDetails {

    @Id
    @Column
    private String clientId;

    @Column
    private String resourceIds;

    @Column
    private String clientSecret;

    @Column
    private String scope;

    @Column
    private String authorizedGrantTypes;

    @Column
    private String webServerRedirectUri;

    @Column
    private String authorities;

    @Column
    private Integer accessTokenValidity;

    @Column
    private Integer refreshTokenValidity;

    @Column(length = 4096)
    private String additionalInformation;

    @Column
    private String autoapprove;

    @Builder
    public OAuthClientDetails(String clientId, String resourceIds, String clientSecret, String scope, String authorizedGrantTypes, String webServerRedirectUri, String authorities, Integer accessTokenValidity, Integer refreshTokenValidity, String additionalInformation, String autoapprove) {
        this.clientId = clientId;
        this.resourceIds = resourceIds;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.webServerRedirectUri = webServerRedirectUri;
        this.authorities = authorities;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
        this.additionalInformation = additionalInformation;
        this.autoapprove = autoapprove;
    }
}
