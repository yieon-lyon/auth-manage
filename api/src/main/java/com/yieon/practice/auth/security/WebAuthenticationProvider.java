package com.yieon.practice.auth.security;

import com.yieon.practice.auth.lib.exception.DataNotFoundException;
import com.yieon.practice.auth.web.rest.errors.AuthApiException;
import com.yieon.practice.auth.lib.model.domain.auth.OAuthClientTokens;
import com.yieon.practice.auth.service.TokenService;
import com.yieon.practice.auth.web.rest.vm.AuthorizationForm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : OAuth2.0 접근 provider
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * 2023. 04. 08. yieon : access_token 발급 프로세스 변경 / access_token 갱신 프로세스 변경 (refresh_token grant)
 * </PRE>
 */
@Component
public class WebAuthenticationProvider implements AuthenticationProvider {

    private final Logger log = LoggerFactory.getLogger(WebAuthenticationProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    @Value("${security.oauth2.client.access-token-uri}")
    private String accessTokenUri;

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("#{'${security.oauth2.client.scope}'.trim().split(',')}")
    List<String> scope;

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    @Autowired
    private TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getPrincipal().toString();
//        String password = authentication.getCredentials().toString();
//
//        String token = (String) getAccessToken(username, password).get("access_token");
//        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
//        String claims = jwt.getClaims();
//
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = null;
//        try {
//            map = mapper.readValue(claims, new TypeReference<Map<String, Object>>(){});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        List<String> authorities = (List<String>) map.get("authorities");
//
//        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
//        authorities.stream().forEach(authority -> grantedAuths.add(new SimpleGrantedAuthority(authority)));
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, grantedAuths);
//        Map<String, String> details = new HashMap<>();
//        details.put("access_token", token);
//        authenticationToken.setDetails(details);
//        return authenticationToken;
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * ACCESS_TOKEN 발급
     * @param authorizationForm
     * @return
     */
    public Map getAccessToken(AuthorizationForm authorizationForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", authorizationForm.getAuthorization());

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("username", authorizationForm.getUsername());
        body.add("password", authorizationForm.getPassword());
        body.add("grant_type", "password");
        body.add("client_id", authorizationForm.getClientId());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = restTemplateGenerate();

        Map access = restTemplate.postForObject(accessTokenUri, request, Map.class);
        assert access != null;
        access.put("username", authorizationForm.getUsername());
        access.put("requestClient", authorizationForm.getClientId());
        OAuthClientTokens oAuthClientTokens = this.tokenService.saveOAuthClientToken(access);
        clientTokensCheck(oAuthClientTokens);
        if (oAuthClientTokens.getRevoke()) {
            throw new AuthApiException("Account login is not possible. Please contact the administrator.");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("access_token", oAuthClientTokens.getAccessToken());
        result.put("expires", oAuthClientTokens.getAccessValidDt());
        return result;
    }

    /**
     * ACCESS_TOKEN 갱신
     * @param httpServletRequest
     * @param client
     * @return
     */
    public Map refreshAccessToken(HttpServletRequest httpServletRequest, Map client) {
        String Authorization = httpServletRequest.getHeader("Authorization");
        String access_token = httpServletRequest.getHeader("access_token");
        String clientId = client.get("client_id").toString();
        String email = null;
        if (client.get("email") != null) {
            email = client.get("email").toString();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", Authorization);
        OAuthClientTokens oAuthClientTokens;
        if (email != null) {
            oAuthClientTokens = this.tokenService.findByEmail(client.get("email").toString());
        } else {
            oAuthClientTokens = this.tokenService.findToken(access_token);
        }
        Map<String, Object> reject = new HashMap<>();
        if (oAuthClientTokens == null) {
            reject.put("status", "ACCESS_TOKEN_NONE");
            return reject;
        } else {
            int valid = oAuthClientTokens.getAccessValidDt().compareTo(Instant.now().plusSeconds(300));
            if (valid > 0) {
                reject.put("status", "REFRESH_REJECT");
                return reject;
            }
        }
        assert oAuthClientTokens != null;

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("refresh_token", oAuthClientTokens.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = restTemplateGenerate();

        Map refresh = restTemplate.postForObject(accessTokenUri, request, Map.class);
        assert refresh != null;
        refresh.put("client_id", clientId);
        OAuthClientTokens updatedClientTokens = this.tokenService.refreshOAuthClientToken(oAuthClientTokens.getRefreshToken(), refresh);
        clientTokensCheck(updatedClientTokens);
        if (updatedClientTokens.getRevoke()) {
            throw new AuthApiException("Account login is not possible. Please contact the administrator.");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("access_token", updatedClientTokens.getAccessToken());
        result.put("expires", updatedClientTokens.getAccessValidDt());
        return result;
    }

    /**
     * OAuthClientTokens 예외처리
     * @param oAuthClientTokens
     */
    private void clientTokensCheck(OAuthClientTokens oAuthClientTokens) {
        if (oAuthClientTokens == null) {
            throw new DataNotFoundException("does not exists user token");
        }
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            log.trace("Invalid JWT token trace.", e);
        }
        return false;
    }

    public Authentication getAuthentication(String token) {
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));
        String claims = jwt.getClaims();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> readValue = objectMapper.readValue(claims, new TypeReference<Map<String, Object>>(){});
            String strAuthorities = readValue.get("authorities").toString().replaceAll("[\\[\\]\\s]", "");
            Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(strAuthorities.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            User principal = new User(readValue.get("user_name").toString(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private RestTemplate restTemplateGenerate() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        return restTemplate;
    }
}
