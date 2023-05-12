package com.yieon.practice.auth.lib.specification.account;

import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.specification.EntitySpecificationsBuilder;
import org.springframework.data.jpa.domain.Specification;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.specification
 *     |UserSpecificationsBuilder.java
 * ------------------------
 * summary : UserSpecificationsBuilder
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public final class UserSpecificationsBuilder extends EntitySpecificationsBuilder {

	/**
	 * 
	 * @since 2023. 04. 28.
	 * @author yieon
	 *         yieon@srdnetworks.com
	 * @return
	 * @see com.yieon.practice.auth.lib.specification.EntitySpecificationsBuilder#build()
	 */
	@Override
	public Specification<User> build() {

		if (params.size() == 0)
			return null;

		Specification<User> result = new UserSpecification(params.get(0));

		for (int i = 1; i < params.size(); i++) {
			result = params.get(i)
					.isOrPredicate()
					? Specification.where(result)
					.or(new UserSpecification(params.get(i)))
					: Specification.where(result)
					.and(new UserSpecification(params.get(i)));

		}

		return result;
	}

}
