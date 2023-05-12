package com.yieon.practice.auth.web.rest;

import com.yieon.practice.auth.security.WebAuthenticationProvider;
import com.yieon.practice.auth.security.jwt.JWTFilter;
import com.yieon.practice.auth.web.rest.vm.AuthorizationForm;
import com.yieon.practice.auth.web.rest.errors.AuthApiException;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : OAuth2.0 인증 Controller
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * 2023. 04. 08. yieon : access_token 발급 프로세스 변경 / access_token 갱신 프로세스 변경 (refresh_token grant)
 * </PRE>
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    private final WebAuthenticationProvider webAuthenticationProvider;

    public UserJWTController(WebAuthenticationProvider webAuthenticationProvider) {
        this.webAuthenticationProvider = webAuthenticationProvider;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Map> authorize(HttpServletRequest httpServletRequest, @Valid @RequestBody AuthorizationForm authorizationForm) {
        if (authorizationForm.getClientId().equals(clientId)) {
            authorizationForm.setAuthorization("Basic "+new String(Base64.encodeBase64((clientId+":"+clientSecret).getBytes())));
        } else {
            authorizationForm.setAuthorization(httpServletRequest.getHeader("Authorization"));
        }
        Map result = webAuthenticationProvider.getAccessToken(authorizationForm);
        String jwt = (String) result.get("access_token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/authenticate/refresh")
    public ResponseEntity<Map> refresh(HttpServletRequest httpServletRequest, @Valid @RequestBody Map client) {
        Map result = webAuthenticationProvider.refreshAccessToken(httpServletRequest, client);
        if (result.get("access_token") == null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            String jwt = (String) result.get("access_token");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return new ResponseEntity<>(result, httpHeaders, HttpStatus.OK);
        }
    }
}
