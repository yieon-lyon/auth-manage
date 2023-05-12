package com.yieon.practice.auth.web.rest;

import com.yieon.practice.auth.lib.exception.UserEmailDuplicatedException;
import com.yieon.practice.auth.lib.model.constant.account.UserClient;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.model.dto.account.RoleDto;
import com.yieon.practice.auth.lib.model.dto.account.UserDto;
import com.yieon.practice.auth.lib.util.AccountUtil;
import com.yieon.practice.auth.security.AuthoritiesConstants;
import com.yieon.practice.auth.service.UserService;
import com.yieon.practice.auth.web.rest.form.ClientActivationForm;
import com.yieon.practice.auth.web.rest.form.SearchForm;
import io.github.jhipster.web.util.PaginationUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-04
 * <PRE>
 * ------------------------
 * summary : 사용자 관리
 * ------------------------
 * Revision history
 * 2023-04-04. yieon : Initial creation
 * </PRE>
 */
@RestController
@RequestMapping("/user")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * 권한 목록 조회
     * @return
     */
    @ApiOperation(value="권한 목록 조회", notes="권한 목록을 조회합니다.", response= String.class, responseContainer="List")
    @GetMapping(value = "roles", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getRoles() {
        return userService.getRoles();
    }

    /**
     * 클라이언트 목록 조회
     * @return
     */
    @ApiOperation(value="클라이언트 목록 조회", notes="클라이언트 목록을 조회합니다.", response= String.class, responseContainer="List")
    @GetMapping(value = "clients", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getClients() {
        return userService.getClients();
    }

    /**
     * 사용자 목록 조회
     * @param search
     * @return
     */
    @ApiOperation(value="사용자 목록 조회", notes="전체 또는 특정 조건의 사용자 목록을 조회합니다.", response=UserDto.class, responseContainer="List")
    @PostMapping(value = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<UserDto>> getUserList(@RequestBody SearchForm search) {
        List<String> params = new ArrayList<>();

        if (StringUtils.isNotBlank(search.getEmail())) {
            params.add("email~" + search.getEmail());
        }
        if (StringUtils.isNotBlank(search.getRoleCd())) {
            int idx = search.getRoleCd().indexOf("_");
            params.add("roleCd:"+search.getRoleCd().substring(idx+1));
        }

        if (StringUtils.isNotBlank(search.getClientInfo())) {
            params.add("clientId:"+search.getClientInfo());
        }

//        if (StringUtils.isNotBlank(search.getName()))
//            params.add("name~"+search.getName());

        Sort.Order sort = Sort.Order.asc("createdDt");
        if (search.getSort().equals("desc")) {
            sort = Sort.Order.desc("createdDt");
        }
        Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(sort));
        Specification<User> spec = AccountUtil.generateUserSpecification(String.join(",", params));
        final Page<UserDto> page = userService.findAllUsers(spec, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * 사용자 정보 조회 (by email)
     * @param email
     * @return
     */
    @ApiOperation(value = "사용자 정보 조회", notes = "특정 사용자의 세부정보를 조회합니다.", response = UserDto.class)
    @ApiImplicitParam(name = "email", value = "사용자 이메일", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "{email:.+}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public UserDto getUser(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    /**
     * 사용자 등록
     * @param userDto
     * @return
     */
    @ApiOperation(value = "사용자 추가", notes = "사용자 정보를 추가합니다.", response = UserDto.class )
    @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public UserDto addUser(@RequestBody @Valid UserDto userDto){

        //	사용자 중복 체크
        if (userService.findByEmail(userDto.getEmail()) != null)
            throw new UserEmailDuplicatedException("e-mail address already in use.");

        return userService.createUser(userDto).convertToDTO();
    }

    /**
     * 사용자 정보 수정
     * @param userDto
     * @return
     */
    @ApiOperation(value = "사용자 수정", notes = "사용자 정보를 수정합니다.", response = UserDto.class )
    @PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public UserDto updateUser(@RequestBody @Valid UserDto userDto){
        return userService.updateUser(userDto);
    }

    /**
     * 사용자 활성화 여부 수정
     * 비활성화 상태일 경우 클라이언트 활성화 여부에 관계없이 계정 비활성화 역할
     * @param email
     * @param activated
     */
    @ApiOperation(value = "사용자 활성화 여부 수정", notes = "사용자의 활성화 여부를 수정합니다.", response = Void.class )
    @ApiImplicitParam(name = "email", value = "이메일", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "{email:.+}/activated", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void changActivated(@PathVariable String email, @Valid @RequestBody Boolean activated){
        User user = userService.updateActivated(email, activated);
        if (user != null) {
            if (user.isActivated()) {
//                mailService.sendActivationEmail(user);
            }
        }
    }

    /**
     * 사용자 클라이언트 활성화 여부 수정
     * @param email
     */
    @ApiOperation(value = "사용자 클라이언트 활성화 여부 수정", notes = "사용자의 클라이언트 활성화 여부를 수정합니다.", response = Void.class )
    @ApiImplicitParam(name = "email", value = "이메일", required = true, dataType = "String", paramType = "path")
    @PutMapping(value = "{email:.+}/client-activated", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void changeClientActivated(@PathVariable String email, @Valid @RequestBody ClientActivationForm activationForm) throws Exception {
        userService.updateClientActivated(email, activationForm.getClientInfo(), activationForm.getActivated());
    }

    /**
     * 사용자 정보 삭제
     * @param email
     */
    @ApiOperation(value = "사용자 정보 삭제(비활성화)", notes = "사용자 정보를 삭제(비활성화)합니다.")
    @ApiImplicitParam(name = "email", value = "사용자 이메일", required = true, dataType = "String", paramType = "path")
    @DeleteMapping(value = "{email:.+}/delete")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void deleteUser(@PathVariable String email){
        userService.deleteUser(email);
    }

    /**
     * 사용자 정보 삭제 취소
     * @param email
     */
    @ApiOperation(value = "사용자 비활성화 해제", notes = "사용자 비활성화 상태를 해제합니다.")
    @ApiImplicitParam(name = "email", value = "사용자 이메일", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "{email:.+}/repair")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void repairUser(@PathVariable String email){
        userService.repairUser(email);
    }

    /**
     * 사용자 목록 삭제
     * @param emails
     */
    @ApiOperation(value = "사용자 정보 일괄삭제(비활성화)", notes = "사용자 정보를 일괄삭제(비활성화)합니다.")
    @ApiImplicitParam(name = "emails", value = "일괄 삭제(비활성화)할 이메일 목록", required = true, allowMultiple = true, dataType = "String", paramType = "body")
    @DeleteMapping(value = "deletes", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public void deleteUsers(@RequestBody List<String> emails) {
        userService.deleteUsers(emails);
    }
}
