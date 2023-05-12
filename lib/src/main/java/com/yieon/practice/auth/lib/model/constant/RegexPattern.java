package com.yieon.practice.auth.lib.model.constant;

import com.yieon.practice.auth.lib.specification.SearchOperation;
import com.google.common.base.Joiner;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.model.constant
 *     |RegexPattern.java
 * ------------------------
 * summary : 정규표현식 상수 패턴 클래스
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class RegexPattern {
	
	private static final String operationSetExper = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET);
	
    public static final String DEFAULT_PATTERN = "(\\w+?)(" + operationSetExper + ")([a-zA-Zㄱ-ㅎ가-힣0-9_.@-]+?),";

	public static final String PUNCT_PATTERN = "(\\p{Punct}?)([a-zA-Zㄱ-ㅎ가-힣0-9]+?)(" + operationSetExper + ")(\\p{Punct}?)([a-zA-Zㄱ-ㅎ가-힣0-9]+?)(\\p{Punct}?),";

	public static final String EMAIL_PATTERN = "([a-zA-Z0-9]+?)(" + operationSetExper + ")([a-zA-Z0-9]+?[@][0-9a-zA-Z]+?[.][a-zA-Z]{2,3}$)";

	public static final String PHONE_NUMBER_PATTERN = "(\\w+?)(" + operationSetExper + ")(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})";
	
	public static final String BIZ_NUMBER_PATTERN = "(\\w+?)(" + operationSetExper + ")(\\d{3})(\\d{2})(\\d{5})";
	
	public static final String NUMBERS_ONLY_PATTERN = "(\\w+?)(" + operationSetExper + ")(^[0-9]*$)";
	
}
