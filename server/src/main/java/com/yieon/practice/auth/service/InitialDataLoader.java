package com.yieon.practice.auth.service;

import com.yieon.practice.auth.lib.model.constant.account.UserPrivilege;
import com.yieon.practice.auth.lib.model.constant.account.UserRole;
import com.yieon.practice.auth.lib.model.domain.account.*;
import com.yieon.practice.auth.lib.model.domain.auth.OAuthClientDetails;
import com.yieon.practice.auth.lib.repository.account.*;
import com.yieon.practice.auth.lib.repository.auth.OAuthClientDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.service
 *     |InitialDataLoader.java
 * ------------------------
 * summary : OAuth2.0 초기 데이터 Loader
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
@Component
public class InitialDataLoader implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private OAuthClientDetailsRepository oAuthClientDetailsRepository;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private ClientInfoRepository clientInfoRepository;
	@Autowired
	private UserActivationRepository userActivationRepository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		generatedUser();
		generatedOAuthClientDetails();
	}

	@Transactional
	protected User createUserIfNotFound(String email, String password, Boolean activated, Boolean disabled, Instant createdDt, Instant lstLoginDt, Instant pwChangedDt, List<Role> roles, List<ClientInfo> clients) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		User user = User.builder()
						.email(email)
						.password(passwordEncoder.encode(password))
						.activated(activated)
						.disabled(disabled)
						.createdDt(createdDt)
						.lstLoginDt(lstLoginDt)
						.pwChangedDt(pwChangedDt)
						.roles(roles)
						.clients(clients)
						.build();
		userRepository.save(user);
		return user;
	}

	@Transactional
	protected Privilege createPrivilegeIfNotFound(String code, String name) {
		Privilege privilege = privilegeRepository.findById(code).orElse(null);
		if (privilege == null) {
			privilege = new Privilege();
			privilege.setPrivilegeCode(code);
			privilege.setPrivilegeName(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	protected void createRoleIfNotFound(String code, String name, Collection<Privilege> privileges) {
		Role role = roleRepository.findById(code).orElse(null);
		if (role == null) {
			role = new Role();
			role.setRoleCd(code);
			role.setRoleNm(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
	}

	@Transactional
	protected ClientInfo createClientInfoIfNotFound(String clientId, String clientPw) {
		ClientInfo clientInfo = clientInfoRepository.findById(clientId).orElse(null);
		if (clientInfo == null) {
			clientInfo = new ClientInfo();
			clientInfo.setClientId(clientId);
			clientInfo.setClientPw(clientPw);
			clientInfoRepository.save(clientInfo);
		}
		return clientInfo;
	}

	@Transactional
	protected UserActivation createUserActivationIfNotFound(Long id, User user, ClientInfo clientInfo, Boolean activated) {
		UserActivation userActivation = userActivationRepository.findById(id).orElse(null);
		if (userActivation == null) {
			userActivation = new UserActivation();
			userActivation.setUser(user);
			userActivation.setClientInfo(clientInfo);
			userActivation.setActivated(activated);
			userActivationRepository.save(userActivation);
		}
		return userActivation;
	}

	@Transactional
	protected void createOAuthClientDetailsIfNotFound(String clientId, String clientPw, String scope, String grantTypes, String authorities, boolean autoapprove) {
		OAuthClientDetails details = oAuthClientDetailsRepository.findById(clientId).orElse(null);
		if (details == null) {
			details = OAuthClientDetails.builder()
					.clientId(clientId)
					.clientSecret(clientPw)
					.scope(scope)
					.authorizedGrantTypes(grantTypes)
					.authorities(authorities)
					.autoapprove((autoapprove)?"true":"false")
					.accessTokenValidity(1800)
					.refreshTokenValidity(2592000)
					.build();
			oAuthClientDetailsRepository.save(details);
		}
	}

	private void generatedUser() {
		if (userRepository.count() != 0) {
			return;
		}

		Privilege create = createPrivilegeIfNotFound(UserPrivilege.PRIVILEGE_CREATE.privilege, UserPrivilege.PRIVILEGE_CREATE.description);
		Privilege read = createPrivilegeIfNotFound(UserPrivilege.PRIVILEGE_READ.privilege, UserPrivilege.PRIVILEGE_READ.description);
		Privilege delete = createPrivilegeIfNotFound(UserPrivilege.PRIVILEGE_DELETE.privilege, UserPrivilege.PRIVILEGE_DELETE.description);
		Privilege update = createPrivilegeIfNotFound(UserPrivilege.PRIVILEGE_UPDATE.privilege, UserPrivilege.PRIVILEGE_UPDATE.description);

		List<Privilege> fullPrivileges = Arrays.asList(create, read, delete, update);
		List<Privilege> userPrivileges = Arrays.asList(create, read, update);
		List<Privilege> guestPrivileges = Arrays.asList(read);

		createRoleIfNotFound(UserRole.ROLE_SYSADMIN.role, "시스템 관리자", fullPrivileges);
		createRoleIfNotFound(UserRole.ROLE_ADMIN.role, "관리자", fullPrivileges);
		createRoleIfNotFound(UserRole.ROLE_USER.role, "사용자", userPrivileges);
		createRoleIfNotFound(UserRole.ROLE_GUEST.role, "게스트", guestPrivileges);

		Role sysAdminRole = roleRepository.findById(UserRole.ROLE_SYSADMIN.role).orElse(null);
		Role adminRole = roleRepository.findById(UserRole.ROLE_ADMIN.role).orElse(null);
		Role userRole = roleRepository.findById(UserRole.ROLE_USER.role).orElse(null);
		Role guestRole = roleRepository.findById(UserRole.ROLE_GUEST.role).orElse(null);

		ClientInfo auth = createClientInfoIfNotFound("auth", "auth1004");
		ClientInfo auth_api = createClientInfoIfNotFound("auth-api");

		List<ClientInfo> allClients = Arrays.asList(auth, auth_api);
		User admin = createUserIfNotFound("admin@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User user1 = createUserIfNotFound("user@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User yieon = createUserIfNotFound("parrotbill@naver.com", "yieon", false, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User user2 = createUserIfNotFound("user02@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user3 = createUserIfNotFound("user03@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user4 = createUserIfNotFound("user04@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user5 = createUserIfNotFound("user05@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user6 = createUserIfNotFound("user06@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user7 = createUserIfNotFound("user07@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user8 = createUserIfNotFound("user08@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user9 = createUserIfNotFound("user09@test.com", "user", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User user10 = createUserIfNotFound("user10@test.com", "1q2w3e4r1!", true, false, Instant.now(), Instant.now(), null, Collections.singletonList(userRole), allClients);
		User admin2 = createUserIfNotFound("admin02@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin3 = createUserIfNotFound("admin03@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin4 = createUserIfNotFound("admin04@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin5 = createUserIfNotFound("admin05@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin6 = createUserIfNotFound("admin06@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin7 = createUserIfNotFound("admin07@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User admin8 = createUserIfNotFound("admin08@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);
		User great = createUserIfNotFound("great@test.com", "admin", true, false, Instant.now(), Instant.now(), null, Arrays.asList(sysAdminRole, adminRole, userRole), allClients);


		List<User> userList = new ArrayList<>();
		userList.add(admin); userList.add(admin2); userList.add(admin3); userList.add(admin4);
		userList.add(admin5); userList.add(admin6); userList.add(admin7); userList.add(admin8); userList.add(great);
		userList.add(user1); userList.add(user2); userList.add(user3); userList.add(user4); userList.add(user5);
		userList.add(user6); userList.add(user7); userList.add(user8); userList.add(user9); userList.add(user10);
		userList.add(yieon);

		AtomicReference<Long> index = new AtomicReference<>(1L);
		userList.forEach(user -> {
			for (ClientInfo client : allClients) {
				createUserActivationIfNotFound(index.getAndSet(index.get() + 1), user, client, true);
			}
		});
	}

	private void generatedOAuthClientDetails() {
		boolean alreadyInitialized = (oAuthClientDetailsRepository.count() != 0);

		if (alreadyInitialized)
			return;

		String fullScope = Arrays.stream(UserPrivilege.values())
				.map(privilege -> privilege.privilege.replace("PRIVILEGE_", "").toLowerCase())
				.collect(Collectors.joining(","));

		createOAuthClientDetailsIfNotFound("auth", "auth1004", fullScope, "client_credentials,refresh_token", UserRole.ROLE_ADMIN.role, true);
		createOAuthClientDetailsIfNotFound("auth-api", "auth-api", fullScope, "password,refresh_token", UserRole.ROLE_ADMIN.role, true);
	}

}
