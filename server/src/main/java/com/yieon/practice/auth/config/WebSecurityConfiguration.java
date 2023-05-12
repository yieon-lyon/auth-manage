package com.yieon.practice.auth.config;

import com.yieon.practice.auth.service.OAuthClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.config
 *     |WebSecurityConfiguration.java
 * ------------------------
 * summary : Spring Security Authentication Configuration
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Profile("!https")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuthClientDetailsService userDetailService;

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	/**
	 * 데이터베이스 인증 Provider
	 * 
	 * @return
	 */
	@Bean
	public CustomAuthenticationProvider authenticationProvider() {
		CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		return authenticationProvider;
	}

	@Override
    protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().disable()
				.and().authorizeRequests().antMatchers("/token/**", "/api/**", "/v2/api-docs", "/swagger-resources/**",
																	"/swagger-ui.html", "/webjars/**", "/swagger/**").permitAll()
				.anyRequest().authenticated();
	}
}
