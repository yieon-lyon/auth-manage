package com.yieon.practice.auth.lib.model.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : CLIENT INFORMATION DOMAIN
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table(name = "CLIENT_INFO")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "user")
@JsonIgnoreProperties(value = "user")
public class ClientInfo implements Serializable {

    @Id
    @Column(name = "CLIENT_ID", nullable = false)
    private String clientId;

    @Column(name = "CLIENT_PW", nullable = false)
    private String clientPw;

    @ManyToMany(mappedBy = "clients")
    private Collection<User> user;
}
