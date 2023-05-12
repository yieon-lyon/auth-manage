package com.yieon.practice.auth.lib.model;

import com.yieon.practice.auth.lib.model.domain.account.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-20
 * <PRE>
 * com.yieon.practice.auth.model
 *     |ClientUserDetails.java
 * ------------------------
 * summary : 스프링 시큐리티에서 인가된 사용자로 처리되는 사용자 정보
 * ------------------------
 * Revision history
 * 2023. 04. 20. yieon : Initial creation
 * </PRE>
 */
@Data
public class ClientUserDetails implements UserDetails {

	private static final long serialVersionUID = -370099041079828415L;
	
	private String username;
	private String password;
	private List<String> roles;
	private Set<String> scopes;

	public ClientUserDetails(User user) {
		this.username = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles().stream().map(role -> String.valueOf(role.getRoleCd())).collect(Collectors.toList());
		this.scopes = new HashSet<>();
		user.getRoles().forEach(role -> {
			List<String> privileges = role.getPrivileges().stream().map(privilege -> String.valueOf(privilege.getPrivilegeCode())).collect(Collectors.toList());
			privileges.forEach(privilege -> scopes.add(privilege.replace("PRIVILEGE_", "").toLowerCase()));
		});
	}

	/**
	 * 해당 계정이 보유한 권한 목록을 리턴
	 *
	 * @return
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
		return authorities;
	}

	/**
	 * 계정 만료 여부 리턴
	 * - true 를 리턴할 경우 만료되지 않았음을 의미
	 *
	 * @return
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * 계정 잠김 여부 리턴
	 * - true 를 리턴할 경우 해당 계정이 잠겨있지 않음을 의미
	 *
	 * @return
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 해당 계정의 자격 만료 여부 리턴
	 * - true 를 리턴할 경우 해당 계정의 자격이 아직 만료되지 않았음을 의미
	 *
	 * @return
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * 계정 사용여부 리턴
	 * - true 를 리턴할 경우 사용 가능한 계정임을 의미
	 * @return
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}
}
