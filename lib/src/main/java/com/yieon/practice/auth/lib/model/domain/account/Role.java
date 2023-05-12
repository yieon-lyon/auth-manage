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
 * @since 2023-04-14
 * <PRE>
 *  com.yieon.practice.auth.model.domain.scm
 *  |Role.java
 * ------------------------
 * summary : ROLE DOMAIN
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table(name = "ROLE")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"users", "privileges"})
@JsonIgnoreProperties(value = {"users", "privileges"})
public class Role implements Serializable {

	@Id
	@Column(name = "ROLE_CD")
	private String roleCd;

	@Column(name = "ROLE_NM")
	private String roleNm;

	@ManyToMany(mappedBy = "roles")
	private Collection<User> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "ROLE_PRIVILEGE", joinColumns = @JoinColumn(name = "ROLE_CD", referencedColumnName = "ROLE_CD"),
				inverseJoinColumns = @JoinColumn(name = "PRIVILEGE_CD", referencedColumnName = "PRIVILEGE_CD"))
	private Collection<Privilege> privileges;

}
