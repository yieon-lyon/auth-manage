package com.yieon.practice.auth.lib.model.domain.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-07
 * <PRE>
 *  com.yieon.practice.auth.model.domain.auth
 *  |OAuthClientTokens.java
 * ------------------------
 * summary : OAuth2.0 Client Token 정보
 * ------------------------
 * Revision history
 * 2023. 04. 07. yieon : Initial creation
 * </PRE>
 */
@Entity
@Data
@NoArgsConstructor
public class OAuthClientTokens {

    @Id
    @Column
    private String email;

    @Size(max = 1000)
    @Column(length = 1000)
    private String accessToken;

    @Size(max = 1000)
    @Column(length = 1000)
    private String refreshToken;

    @Column
    private Instant accessValidDt;

    @Column
    private Instant refreshValidDt;

    @Column
    private Boolean revoke;

    @Column
    private String requestClient;

}
