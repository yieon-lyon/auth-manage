package com.yieon.practice.auth.lib.repository.account;

import com.yieon.practice.auth.lib.model.domain.account.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * ------------------------
 * summary : Privilege jpa Repository
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, String> {

}
