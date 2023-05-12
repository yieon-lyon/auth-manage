package com.yieon.practice.auth.service;

import com.yieon.practice.auth.lib.model.domain.auth.OAuthClientTokens;
import com.yieon.practice.auth.lib.repository.auth.OAuthClientTokensRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-08
 * <PRE>
 * ------------------------
 * summary : Token Service
 * ------------------------
 * Revision history
 * 2023-04-08. yieon : Initial creation
 * </PRE>
 */
@Service
public class TokenService {

    private final OAuthClientTokensRepository oAuthClientTokensRepository;

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    public TokenService(OAuthClientTokensRepository oAuthClientTokensRepository) {
        this.oAuthClientTokensRepository = oAuthClientTokensRepository;
    }

    @Transactional
    public OAuthClientTokens saveOAuthClientToken(Map tokenForm) {
        OAuthClientTokens oAuthClientTokens = oAuthClientTokensRepository.findByEmail(tokenForm.get("username").toString());
        try {
            if (oAuthClientTokens == null) {
                oAuthClientTokens = new OAuthClientTokens();
                oAuthClientTokens.setEmail(tokenForm.get("username").toString());
                oAuthClientTokens.setRevoke(false);
            }
            oAuthClientTokens.setAccessToken(tokenForm.get("access_token").toString());
            oAuthClientTokens.setRefreshToken(tokenForm.get("refresh_token").toString());
            oAuthClientTokens.setAccessValidDt(getAccessTokenValidDt(tokenForm.get("access_token").toString()));
            oAuthClientTokens.setRefreshValidDt(Instant.now().plusSeconds(2592000));
            oAuthClientTokens.setRequestClient(tokenForm.get("requestClient").toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return oAuthClientTokensRepository.save(oAuthClientTokens);
    }

    @Transactional
    public OAuthClientTokens refreshOAuthClientToken(String refreshToken, Map refreshForm) {
        OAuthClientTokens oAuthClientTokens = oAuthClientTokensRepository.findByRefreshToken(refreshToken);
        if (oAuthClientTokens == null) {
            return null;
        }
        oAuthClientTokens.setAccessToken(refreshForm.get("access_token").toString());
        oAuthClientTokens.setRequestClient(refreshForm.get("client_id").toString());
        oAuthClientTokens.setAccessValidDt(Instant.now().plusSeconds((Integer) refreshForm.get("expires_in")));
        return oAuthClientTokensRepository.save(oAuthClientTokens);
    }

    @Transactional
    public OAuthClientTokens findToken(String accessToken) {
        return oAuthClientTokensRepository.findByAccessToken(accessToken);
    }

    @Transactional
    public OAuthClientTokens findByEmail(String email) {
        return oAuthClientTokensRepository.findByEmail(email);
    }

    private Instant getAccessTokenValidDt(String accessToken) throws JsonProcessingException {
        Jwt jwt = JwtHelper.decodeAndVerify(accessToken, new RsaVerifier(publicKey));
        String claims = jwt.getClaims();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> readValue = objectMapper.readValue(claims, new TypeReference<Map<String, Object>>() {});
        return Instant.ofEpochSecond((Integer) readValue.get("exp"));
    }
}
