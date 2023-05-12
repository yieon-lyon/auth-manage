package com.yieon.practice.auth.web.rest;

import com.yieon.practice.auth.lib.exception.DataNotFoundException;
import com.yieon.practice.auth.lib.exception.UserEmailDuplicatedException;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.model.dto.account.UserDto;
import com.yieon.practice.auth.security.UserNotActivatedException;
import com.yieon.practice.auth.service.UserService;
import com.yieon.practice.auth.web.rest.errors.InvalidPasswordException;
import com.yieon.practice.auth.web.rest.form.PasswordForm;
import com.yieon.practice.auth.web.rest.form.RegisterForm;
import com.yieon.practice.auth.web.rest.form.ResetPasswordForm;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * 회원가입
     * @param registerForm
     * @return
     */
    @ApiOperation(value = "회원가입", notes = "사용자 정보를 등록합니다.", response = UserDto.class )
    @PostMapping(value = "register", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto createUser(@RequestBody @Valid RegisterForm registerForm){

        if (userService.findByEmail(registerForm.getEmail()) != null)
            throw new UserEmailDuplicatedException("e-mail address already in use.");

        return userService.registerUser(registerForm).convertToDTO();
    }

    /**
     * 사용자 비밀번호 변경
     * @return
     */
    @ApiOperation(value = "사용자 비밀번호 변경", notes = "특정 사용자의 비밀번호를 변경합니다.", response = UserDto.class)
    @PutMapping(value = "change-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDto updateUserPwByEmailAndPassword(@RequestBody PasswordForm passwordForm) {
        return userService.updatePassword(passwordForm);
    }

    /**
     * 사용자 활성화
     * 관리자의 승인과정을 거치지 않고(메일발송없음) 개별 클라이언트에서 활성화 프로세스 동작 기능
     * @param email
     */
    @ApiOperation(value = "사용자 활성화", notes = "사용자를 활성화합니다.", response = Void.class )
    @ApiImplicitParam(name = "email", value = "이메일", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "{email:.+}/active", produces = MediaType.APPLICATION_JSON_VALUE)
    public void changActivated(@PathVariable String email){
        userService.updateActivated(email, true);
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @ApiOperation(value = "비밀번호 재설정 요청", notes = "해당 이메일의 비밀번호 재설정에 필요한 reset key 생성을 요청한다.", response = Void.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mail", value = "이메일", required = true, paramType = "query"),
        @ApiImplicitParam(name = "type", value = "요청타입", required = true, paramType = "query")
    })
    @PostMapping(path = "reset-password/init")
    public void requestPasswordReset(@RequestParam String mail, @RequestParam String type) {
        UserDto user = userService.findByEmail(mail);
        if (user != null) {
//            mailService.sendPasswordResetMail(userService.requestPasswordReset(mail), type);
        } else {
            throw new DataNotFoundException("email not found");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param newPassword the new password.
     * @param key the generated key
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @ApiOperation(value = "비밀번호 재설정", notes = "비밀번호를 재설정한다.", response=Void.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "newPassword", value = "새로운 비밀번호", required = true, paramType = "query"),
        @ApiImplicitParam(name = "key", value = "리셋 키", required = true, paramType = "query")
    })
    @PostMapping(path = "reset-password/finish")
    public void finishPasswordReset(@RequestParam String newPassword, @RequestParam String key) {
        if (newPassword.isEmpty()) {
            throw new InvalidPasswordException();
        }
        User user = userService.completePasswordReset(newPassword, key);
        if (user == null) {
            throw new UserNotActivatedException("No user was found for this reset key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDto getAccount() {
        return userService.getUserWithAuthorities();
//            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

}
