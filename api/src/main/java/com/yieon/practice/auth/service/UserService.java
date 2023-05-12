package com.yieon.practice.auth.service;

import com.yieon.practice.auth.config.Constants;
import com.yieon.practice.auth.lib.error.ErrorCode;
import com.yieon.practice.auth.lib.exception.DataNotFoundException;
import com.yieon.practice.auth.lib.model.constant.account.UserClient;
import com.yieon.practice.auth.lib.model.constant.account.UserRole;
import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.model.domain.account.Role;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.model.domain.account.UserActivation;
import com.yieon.practice.auth.lib.model.dto.account.RoleDto;
import com.yieon.practice.auth.lib.model.dto.account.UserDto;
import com.yieon.practice.auth.lib.repository.account.ClientInfoRepository;
import com.yieon.practice.auth.lib.repository.account.RoleRepository;
import com.yieon.practice.auth.lib.repository.account.UserActivationRepository;
import com.yieon.practice.auth.lib.repository.account.UserRepository;
import com.yieon.practice.auth.security.SecurityUtils;
import com.yieon.practice.auth.web.rest.errors.AuthApiException;
import com.yieon.practice.auth.web.rest.errors.InvalidPasswordException;
import com.yieon.practice.auth.web.rest.form.PasswordForm;
import com.yieon.practice.auth.web.rest.form.RegisterForm;
import io.github.jhipster.security.RandomUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final ClientInfoRepository clientInfoRepository;

    private final UserActivationRepository userActivationRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, ClientInfoRepository clientInfoRepository, UserActivationRepository userActivationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.clientInfoRepository = clientInfoRepository;
        this.userActivationRepository = userActivationRepository;
    }

    public List<String> getRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(Role::getRoleCd).collect(Collectors.toList());
    }

    public List<String> getClients() {
        List<ClientInfo> clients = clientInfoRepository.findAll();
        return clients.stream().map(ClientInfo::getClientId).collect(Collectors.toList());
    }

    /**
     * 로그인 정보 조회
     * @return
     */
    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities() {
        if (SecurityUtils.getCurrentUserLogin().isPresent() && !SecurityUtils.getCurrentUserLogin().get().equals(Constants.ANONYMOUS_USER)) {
            User user = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithRolesByEmail).get();
            user.setClients(clientInfoRepository.findAllByUser(user));
            return user.convertToDTO();
        } else return null;
    }
    /**
     * 전체 사용자 목록 조회
     * @return
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> result = new ArrayList<>();
        users.forEach(user -> {
            user.setClients(clientInfoRepository.findAllByUser(user));
            UserDto userDto = user.convertToDTO();
            List<UserActivation> activations = userActivationRepository.findAllByUser(user);
            Map<String, Boolean> userActivationMap = new HashMap<>();
            for (UserActivation activation : activations) {
                userActivationMap.put(activation.getClientInfo().getClientId(), activation.getActivated());
            }
            userDto.setAssignedUserActivations(userActivationMap);
            result.add(userDto);
        });
        return result;
    }

    /**
     * 조건별 사용자 목록 조회
     * @param spec
     * @return
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    public Page<UserDto> findAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = userRepository.findAll(spec, pageable);
        List<UserActivation> activations = userActivationRepository.findAllByUserIn(users.stream().collect(Collectors.toList()));
        users.forEach(user -> {
            user.setClients(clientInfoRepository.findAllByUser(user));

        });
        return convertPageToDTO(users, activations);
    }

    /**
     * 이메일 사용자 정보 조회
     * @param email
     * @return
     */
    public UserDto findByEmail(String email) {
        User user = userRepository.findById(email).orElse(null);
        if (user != null) {
            List<Role> roles = new ArrayList<>();
            user.getRoles().forEach(role -> {
                if (!roles.contains(role)) {
                    roles.add(role);
                }
            });
            user.setRoles(roles);
            user.setClients(clientInfoRepository.findAllByUser(user));
            UserDto result = user.convertToDTO();
            List<UserActivation> activations = userActivationRepository.findAllByUser(user);
            Map<String, Boolean> userActivationMap = new HashMap<>();
            for (UserActivation activation : activations) {
                userActivationMap.put(activation.getClientInfo().getClientId(), activation.getActivated());
            }
            result.setAssignedUserActivations(userActivationMap);
            return result;
        } else {
            return null;
        }
    }

    /**
     * 사용자 정보 등록
     * @param userDto
     * @return
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public User createUser(UserDto userDto) {
        List<Role> roles = roleRepository.findRolesByRoleCdIn(userDto.getAssignedRoles());
        List<ClientInfo> clients = clientInfoRepository.findClientInfosByClientIdIn(userDto.getAssignedClients());

        // 비밀번호 랜덤 생성 (영 대,소문자 + 숫자)
        String tempPassword = RandomStringUtils.randomAlphanumeric(10);
        User user = User.builder()
            .email(userDto.getEmail())
            .password(passwordEncoder.encode(tempPassword))
            .activated(userDto.getActivated())
            .disabled(false)
            .createdDt(Instant.now())
            .lstLoginDt(null)
            .pwChangedDt(null)
            .langKey("en")
            .roles(roles)
            .clients(clients)
            .build();

        userRepository.save(user);
        user.setClients(clientInfoRepository.findAllByUser(user));
        return user;
    }

    /**
     * 회원가입
     * @param registerForm
     * @return
     */
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public User registerUser(RegisterForm registerForm) {

        // default set - role: user / clients: auth
        List<String> userRoles = Collections.singletonList(UserRole.ROLE_USER.role);
        List<String> userClients = Arrays.asList(UserClient.CLIENT_AUTH.value);

        UserDto userDto = UserDto.builder()
                                 .email(registerForm.getEmail())
                                 .password(registerForm.getPassword())
                                 .activated(true)
                                 .disabled(false)
                                 .createdDt(Instant.now())
                                 .lstLoginDt(null)
                                 .pwChangedDt(null)
                                 .assignedRoles(userRoles)
                                 .assignedClients(userClients)
                                 .build();

        List<Role> roles = roleRepository.findRolesByRoleCdIn(userDto.getAssignedRoles());
        List<ClientInfo> clients = clientInfoRepository.findClientInfosByClientIdIn(userDto.getAssignedClients());

        User user = User.builder()
            .email(userDto.getEmail())
            .password(passwordEncoder.encode(userDto.getPassword()))
            .activated(userDto.getActivated())
            .createdDt(Instant.now())
            .lstLoginDt(null)
            .langKey("en")
            .roles(roles)
            .clients(clients)
            .build();

        userRepository.save(user);
        user.setClients(clientInfoRepository.findAllByUser(user));

        for (ClientInfo clientInfo : clientInfoRepository.findAll()) {
            UserActivation userActivation = new UserActivation();
            // 2. 회원가입 시 제약사항 없음
            userActivation.setActivated(true);
            userActivation.setClientInfo(clientInfo);
            userActivation.setUser(user);
            userActivationRepository.save(userActivation);
            /*
            if (registerForm.getClientType().equals(clientInfo.getClientId()) || registerForm.getActivateClients().contains(clientInfo.getClientId())) {
                UserActivation userActivation = new UserActivation();
                userActivation.setActivated(true);
                userActivation.setClientInfo(clientInfo);
                userActivation.setUser(user);
                userActivationRepository.save(userActivation);
            } else {
                UserActivation userActivation = new UserActivation();
                userActivation.setActivated(false);
                userActivation.setClientInfo(clientInfo);
                userActivation.setUser(user);
                userActivationRepository.save(userActivation);
            }
             */
        }
        return user;
    }

    /**
     * 사용자 정보 수정 (권한 및 클라이언트 정보)
     * @param userDto
     * @return
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getEmail()).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));

        List<Role> roles = roleRepository.findRolesByRoleCdIn(userDto.getAssignedRoles());
        List<ClientInfo> clients = clientInfoRepository.findClientInfosByClientIdIn(userDto.getAssignedClients());
        user.setRoles(roles);
        user.setClients(clients);

        user = userRepository.save(user);
        user.setClients(clientInfoRepository.findAllByUser(user));
        return user.convertToDTO();
    }

    /**
     * 사용자 활성화 여부 수정
     * @param email
     * @param activated
     * @return
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional
    public User updateActivated(String email, Boolean activated) {
        User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        user.setActivated(activated);
        user = userRepository.save(user);
        user.setClients(clientInfoRepository.findAllByUser(user));
        return user;
    }

    /**
     * 사용자 클라이언트 활성화 여부 수정
     * @param email
     * @param clientType
     * @param activated
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional
    public void updateClientActivated(String email, String clientType, Boolean activated) throws Exception {
        User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        ClientInfo clientInfo = clientInfoRepository.findById(clientType).orElse(null);
        if (clientInfo == null) {
            throw new Exception("does not exist clientInfo.");
        }
        UserActivation userActivation = userActivationRepository.findByUserAndClientInfo(user, clientInfo);
        if (userActivation == null) {
            throw new Exception("does not exist userActivation.");
        }
        userActivation.setActivated(activated);
        userActivationRepository.save(userActivation);
    }

    /**
     * 사용자 비밀번호 변경
     * @param passwordForm
     * @return
     */
    @Transactional
    public UserDto updatePassword(PasswordForm passwordForm) {
        User user = userRepository.findById(passwordForm.getEmail()).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        if (passwordEncoder.matches(passwordForm.getCurrentPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(passwordForm.getNewPassword()));
            user.setClients(clientInfoRepository.findAllByUser(user));
            user.setPwChangedDt(Instant.now());
            user = userRepository.save(user);
            return user.convertToDTO();
        } else {
            throw new InvalidPasswordException();
        }
    }

    /**
     * 사용자 비활성화 해제
     * @param email 삭제할 사용자의 이메일 주소
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional
    public void repairUser(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        user.setDisabled(false);
        userRepository.save(user);
    }

    /**
     * 사용자 삭제(비활성화)
     * @param email 삭제할 사용자의 이메일 주소
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findById(email).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        user.setDisabled(true);
        userRepository.save(user);
//        userRepository.deleteById(email);
    }

    /**
     * 사용자 일괄 삭제(비활성화)
     *
     * @param emails 삭제할 사용자 이메일 목록
     */
    @PreAuthorize("hasRole(T(com.yieon.practice.auth.lib.model.constant.account.UserRole).ROLE_ADMIN.role)")
    @Transactional
    public void deleteUsers(List<String> emails) {
        List<User> users = userRepository.findByEmailIn(emails);
        for (User user : users) {
            user.setDisabled(true);
            userRepository.save(user);
        }
//        userRepository.deleteByEmailIn(emails);
    }

    /**
     * 비밀번호 초기화 요청
     *
     * @param mail
     * @return
     */
    public User requestPasswordReset(String mail) {
        User user = userRepository.findById(mail).orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND.getMessage()));
        user.setPwResetKey(RandomUtil.generateResetKey());
        user.setPwResetDt(Instant.now());
        userRepository.save(user);
        return user;
    }

    /**
     * 비밀번호 재설정
     * @param newPassword
     * @param key
     * @return
     */
    public User completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        User user = userRepository.findOneByPwResetKey(key).orElse(null);
        if (user != null) {
            if (user.getPwResetDt().isAfter(Instant.now().minusSeconds(86400))) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setPwResetKey(null);
                user.setPwResetDt(null);
                user.setLoginFailCnt(0);
                userRepository.save(user);
            } else {
                throw new AuthApiException("Reset key expiration time has elapsed");
            }
        }
        return user;
    }

    /**
     * 사용자 엔티티 리스트를 DTO 리스트로 변환
     *
     * @param users 사용자 엔티티 리스트
     * @return
     */
    private List<UserDto> convertListToDTO(List<User> users) {
        return users.stream().map(User::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 사용자 엔티티 PAGE 를 DTO 리스트로 변환
     *
     * @param users 사용자 엔티티 PAGE
     * @return
     */
    private Page<UserDto> convertPageToDTO(Page<User> users, List<UserActivation> activations) {
        return users.map(user -> {
            UserDto userDto = user.convertToDTO();
            Map<String, Boolean> userActivationMap = new HashMap<>();
            for (UserActivation activation : activations) {
                if (user.getEmail().equals(activation.getUser().getEmail())) {
                    userActivationMap.put(activation.getClientInfo().getClientId(), activation.getActivated());
                }
            }
            userDto.setAssignedUserActivations(userActivationMap);
            return userDto;
        });
    }
}
