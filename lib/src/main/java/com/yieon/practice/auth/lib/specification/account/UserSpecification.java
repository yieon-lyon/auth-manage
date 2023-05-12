package com.yieon.practice.auth.lib.specification.account;

import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.model.domain.account.Role;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.specification.EntitySpecification;
import com.yieon.practice.auth.lib.specification.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.specification
 *     |UserSpecification.java
 * ------------------------
 * summary : 사용자 조회조건 Specification
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class UserSpecification extends EntitySpecification implements Specification<User> {

	private static final long serialVersionUID = 1L;
	
	private String[] joinKeys = {"roleCode", "activated", "accessWeb", "accessMobile"};

	public UserSpecification(SearchCriteria criteria) {
		super(criteria);
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		SearchCriteria criteria = getCriteria();
		// Email 체크
		if (criteria.getKey().equals("email")) {
			return builder.like(root.get("email"), "%" + criteria.getValue() + "%");
		}
//		if (criteria.getKey().equals("name")) {
//			return builder.like(root.get("name"), "%" + criteria.getValue() + "%");
//		}
		
		if (Arrays.asList(joinKeys).contains(criteria.getKey())) {
			if (criteria.getKey().equals("roleCd")) {
				Join<User, Role> join = root.join("roles", JoinType.INNER);
				return builder.like(join.get("roleCd"), "%"+ criteria.getValue());
			}
			if (criteria.getKey().equals("clientId")) {
				Join<User, ClientInfo> join = root.join("clients", JoinType.INNER);
				return builder.like(join.get("clientId"), "%"+ criteria.getValue());
			}
		}
		
		return predicate(root, query, builder);
	}
}
