package com.yieon.practice.auth.service;

import com.yieon.practice.auth.lib.model.ClientUserDetails;
import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.model.domain.account.UserActivation;
import com.yieon.practice.auth.lib.repository.account.ClientInfoRepository;
import com.yieon.practice.auth.lib.repository.account.UserActivationRepository;
import com.yieon.practice.auth.lib.repository.account.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.service
 *     |OAuthClientDetailsService.java
 * ------------------------
 * summary : OAuth2.0 사용자 상세정보 조회 서비스
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * 2023. 04. 29. yieon : Changed OAuth2.0 authority process (password login)
 * </PRE>
 */
@Service
public class OAuthClientDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClientInfoRepository clientInfoRepository;
	@Autowired
	private UserActivationRepository userActivationRepository;

	@Deprecated
	@Autowired
	private RegistSpreadService registSpreadService;

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		org.springframework.security.core.userdetails.User client = null;
		if (securityContext.getAuthentication() != null) {
			client = (org.springframework.security.core.userdetails.User) securityContext.getAuthentication().getPrincipal();
		}
		User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("UsernameNotFound [" + username + "]"));
		if (user.isDisabled()) {
			throw new Exception("UserLocked [" + username + "]");
		}
		if (!user.isActivated()) {
			throw new NotActiveException("UserNotActivated [" + username + "]");
		}
		if (client == null) {
			return new ClientUserDetails(user);
		} else {
			org.springframework.security.core.userdetails.User finalClient = client;
			List<ClientInfo> clientInfos = clientInfoRepository.findAllByUser(user);
			List<ClientInfo> existsClientInfos = clientInfos.stream().filter(clientInfo -> clientInfo.getClientId().equals(finalClient.getUsername())).collect(Collectors.toList());
			if (existsClientInfos.size() == 1) {
				UserActivation userActivation = userActivationRepository.findByUserAndClientInfo(user, existsClientInfos.get(0));
				if (userActivation != null) {
					if (userActivation.getActivated()) {
						/* Deprecated
						if (user.getLstLoginDt() == null) {
							 registSpreadService.attemptRegisterSpread(client, user);
						} else {}
						 */
						if (user.getLoginFailCnt() == 5) {
							throw new NotActiveException("Exceeded [" + username + "]");
						}
						user.setLstLoginDt(Instant.now());
						userRepository.saveAndFlush(user);
						return new ClientUserDetails(user);
					} else {
                        throw new NotActiveException(Objects.requireNonNull(existsClientInfos.stream().findFirst().orElse(null)).getClientId() + "-NotActivated [" + username + "]");
					}
				} else {
					throw new NotActiveException(Objects.requireNonNull(existsClientInfos.stream().findFirst().orElse(null)).getClientId() + "-NotActivated [" + username + "]");
				}
			} else throw new Exception("There is no client information assigned to the user.");
		}
	}
}
