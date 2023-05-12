package com.yieon.practice.auth.lib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-28
 * <PRE>
 * com.yieon.practice.auth.lib.util
 *     |CommonUtil.java
 * ------------------------
 * summary : CommonUtil 유틸리티
 * ------------------------
 * Revision history
 * 2023. 04. 28. yieon : Initial creation
 * </PRE>
 */
public class CommonUtil {

	/**
	 * 
	 * @since 2023. 04. 28.
	 * @author yieon
	 *         
	 * @param email
	 * @return
	 * 개요 : 이메일 유효성 체크
	 */
	public static boolean isValidEmail(String email) {
	     
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		
		return isNormal;
		
	}
	
}
