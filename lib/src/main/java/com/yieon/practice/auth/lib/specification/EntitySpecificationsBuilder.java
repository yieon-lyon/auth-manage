package com.yieon.practice.auth.lib.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.specification
 *     |EntitySpecificationsBuilder.java
 * ------------------------
 * summary : 엔티티 조회 조건 목록 생성 추상 클래스
 *           - reference : http://www.baeldung.com/rest-api-query-search-language-more-operations
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public abstract class EntitySpecificationsBuilder {

	protected final List<SearchCriteria> params;

	public EntitySpecificationsBuilder() {
		params = new ArrayList<>();
	}

	public final EntitySpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
		return with(null, key, operation, value, prefix, suffix);
	}
	
	public final EntitySpecificationsBuilder with(final String key, final String operation, final Object value) {
		SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
		if (op != null) {
				params.add(new SearchCriteria(key, op, value));
		}
		return this;
	}
	
	public final EntitySpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value) {
		SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
		if (op != null) {
				params.add(new SearchCriteria(orPredicate, key, op, value));
		}
		return this;
	}
	
	public final EntitySpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
		SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
		if (op != null) {
			if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
				final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
				final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

				if (startWithAsterisk && endWithAsterisk) {
					op = SearchOperation.CONTAINS;
				} else if (startWithAsterisk) {
					op = SearchOperation.ENDS_WITH;
				} else if (endWithAsterisk) {
					op = SearchOperation.STARTS_WITH;
				}
			}
			params.add(new SearchCriteria(orPredicate, key, op, value));
		}
		return this;
	}

	public final EntitySpecificationsBuilder with(EntitySpecification spec) {
		params.add(spec.getCriteria());
		return this;
	}

	public final EntitySpecificationsBuilder with(SearchCriteria criteria) {
		params.add(criteria);
		return this;
	}

	public abstract Specification<?> build();

}
