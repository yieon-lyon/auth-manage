package com.yieon.practice.auth.lib.model.domain.account;

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
 *  |Privilege.java
 * ------------------------
 * summary : PRIVILEGE DOMAIN
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table(name = "PRIVILEGE")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = "roles")
public class Privilege implements Serializable {

	@Id
	@Column(name = "PRIVILEGE_CD")
	private String privilegeCode;

	@Column(name = "PRIVILEGE_NM")
	private String privilegeName;

	@ManyToMany(mappedBy = "privileges")
	private Collection<Role> roles;

}
