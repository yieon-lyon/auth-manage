package com.yieon.practice.auth.lib.repository.account;

import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.model.domain.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : Client info jpa Repository
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * 2023. 04. 29. yieon : Add findAllByUser
 * </PRE>
 */
@Repository
public interface ClientInfoRepository extends JpaRepository<ClientInfo, String> {

    List<ClientInfo> findAllByUser(User user);

    List<ClientInfo> findClientInfosByClientIdIn(List<String> clientIds);
}
