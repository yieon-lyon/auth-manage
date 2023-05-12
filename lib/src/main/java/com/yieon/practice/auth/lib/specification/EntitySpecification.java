package com.yieon.practice.auth.lib.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.specification
 *     |EntitySpecificationsBuilder.java
 * ------------------------
 * summary : 엔티티 조회 조건 추상 클래스
 *           - reference : http://www.baeldung.com/rest-api-query-search-language-more-operations
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public abstract class EntitySpecification {

	private SearchCriteria criteria;

	public EntitySpecification(final SearchCriteria criteria) {
		this.criteria = criteria;
	}

	public SearchCriteria getCriteria() {
		return criteria;
	}

	protected Predicate predicate(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		switch (criteria.getOperation()) {
			case EQUALITY:
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			case NEGATION:
				return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
			case GREATER_THAN:
				return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
			case LESS_THAN:
				return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
			case LIKE:
				return builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
			case STARTS_WITH:
				return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
			case ENDS_WITH:
				return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
			case CONTAINS:
				return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
			default:
				return null;
		}
	}

}
