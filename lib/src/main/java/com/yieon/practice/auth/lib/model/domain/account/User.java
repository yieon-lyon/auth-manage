package com.yieon.practice.auth.lib.model.domain.account;

import com.yieon.practice.auth.lib.model.dto.account.UserDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.client.Client;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 *  com.yieon.practice.auth.model.domain.scm
 *  |User.java
 * ------------------------
 * summary : USER DOMAIN
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table(name = "USER")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"roles", "clients"})
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean activated = false;

    @Column(nullable = false)
    private boolean disabled = false;

    @Column(name = "CREATED_DT", nullable = false)
    private Instant createdDt;

    @Column(name = "LST_LOGIN_DT")
    private Instant lstLoginDt;

    @Column(name = "LOGIN_FAIL_CNT", columnDefinition = " NUMERIC(5) DEFAULT 0")
    private Integer loginFailCnt = 0;

    @Column(name = "LST_LOGIN_FAIL_DT")
    private Instant lstLoginFailDt;

    @Column(name = "PASSWORD_CHANGED_DT")
    private Instant pwChangedDt;

    @Column(name = "PASSWORD_RESET_KEY")
    private String pwResetKey;

    @Column(name = "PASSWORD_RESET_DT")
    private Instant pwResetDt;

    @Size(min = 2, max = 10)
    @Column(name = "LANGUAGE_KEY", length = 10)
    private String langKey;

    @Setter
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_CD", referencedColumnName = "ROLE_CD"))
    private Collection<Role> roles;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_CLIENT", joinColumns = @JoinColumn(name = "EMAIL", referencedColumnName = "EMAIL"),
            inverseJoinColumns = @JoinColumn(name = "CLIENT_ID", referencedColumnName = "CLIENT_ID"))
    private Collection<ClientInfo> clients;

    @Builder
    public User(String email, String password, boolean activated, boolean disabled, Instant createdDt, Instant lstLoginDt, Instant pwChangedDt, String langKey, Collection<Role> roles, Collection<ClientInfo> clients) {
        this.email = email;
        this.password = password;
        this.activated = activated;
        this.disabled = disabled;
        this.createdDt = createdDt;
        this.lstLoginDt = lstLoginDt;
        this.pwChangedDt = pwChangedDt;
        this.langKey = langKey;
        this.roles = roles;
        this.clients = clients;
    }

    public UserDto convertToDTO() {
        List<String> assignedRoles = roles.stream().map(Role::getRoleCd).collect(Collectors.toList());
        List<String> assignedClients = clients.stream().map(ClientInfo::getClientId).collect(Collectors.toList());
        return UserDto.builder()
                      .email(email)
                      .password(password)
                      .activated(activated)
                      .disabled(disabled)
                      .createdDt(createdDt)
                      .lstLoginDt(lstLoginDt)
                      .pwChangedDt(pwChangedDt)
                      .assignedRoles(assignedRoles)
                      .assignedClients(assignedClients)
                      .build();
    }
}
