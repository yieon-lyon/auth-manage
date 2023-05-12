package com.yieon.practice.auth.config;

import com.yieon.practice.auth.lib.security.CustomAccessDecisionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * com.yieon.practice.auth.config
 *     |ResourceServerConfiguration.java
 * ------------------------
 * summary : OAuth2.0 리소스 서버 설정
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Value("${spring.application.name}")
    private String resourceId;

    @Value("${security.oauth2.resource.jwt.key-value}")
    private String publicKey;

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new ClientDetailsService() {
            @Override
            public ClientDetails loadClientByClientId(String clientId) {
                if (clientId.equals(resourceId)|| clientId.equals("auth")) {
                    return null;
                } else {
                    throw new ClientRegistrationException("Client not valid: " + clientId);
                }
            }
        };
    }

    @Bean
    public TokenStore tokenStoreForResource() {
        return new JwtTokenStore(accessTokenConverterForResource());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverterForResource() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServiceForResource() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStoreForResource());
        defaultTokenServices.setClientDetailsService(clientDetailsService());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
//      Swagger 관련 요청을 제외한 모든 요청에 인증이 필요하도록 설정
//		http.authorizeRequests().antMatchers(AuthRestUrl.AUTH_WHITELIST).permitAll().anyRequest().authenticated().accessDecisionManager(accessDecisionManager());
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
        resources.tokenServices(tokenServiceForResource());
        resources.tokenStore(tokenStoreForResource());
    }

    @Bean
    public CustomAccessDecisionManager accessDecisionVoter() {
        return new CustomAccessDecisionManager();
    }

    /**
     * 접근 허용을 결정하는 AccessDecisionManager Bean
     * @return
     */
    @Bean
    public AccessDecisionManager accessDecisionManager() {
		/*
			- WebExpressionVoter : SpEL (Spring Expression Language)을 사용하여 @PreAuthorize 어노테이션을 사용하여 요청에 대한 권한을 부여할 수 있다.
			- RoleVoter			 : configuration 속성 값중 "ROLE_"로 시작하는 값이 있다면 허용 여부를 판단. Authentication 객체의 GrantedAuthority 목록에서 검색함
			- AuthenticatedVoter : Authentication 객체의 인증 레벨을 기반으로 허용 여부를 판단.
		 */
        List decisionVoters = Arrays.asList(
                new RoleVoter(),
                accessDecisionVoter()
        );
        return new UnanimousBased(decisionVoters);
    }

}
