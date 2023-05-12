package com.yieon.practice.auth.lib.repository.account;

import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.model.domain.account.User;
import com.yieon.practice.auth.lib.model.domain.account.UserActivation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * ------------------------
 * summary : User Activation jpa Repository
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * 2023. 04. 29. yieon : Add findByUserAndClientInfo
 * </PRE>
 */
@Repository
public interface UserActivationRepository extends JpaRepository<UserActivation, Long> {

    List<UserActivation> findAllByUser(User user);

    List<UserActivation> findAllByUserIn(List<User> user);

    UserActivation findByUserAndClientInfo(User user, ClientInfo clientInfo);
}
