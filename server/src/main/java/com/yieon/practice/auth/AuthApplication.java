package com.yieon.practice.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth
 *     |AuthApplication.java
 * ------------------------ 
 * summary : OAuth2.0 Entry point
 * ------------------------ 
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
@EnableAuthorizationServer
//@EnableOAuth2Client
//@EnableOAuth2Sso
@EntityScan("com.yieon.practice.lib.model.domain.auth")
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@CrossOrigin(origins = "*")
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
