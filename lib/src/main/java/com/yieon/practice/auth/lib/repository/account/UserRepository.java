package com.yieon.practice.auth.lib.repository.account;

import com.yieon.practice.auth.lib.model.domain.account.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * ------------------------
 * summary : User jpa Repository
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

//    List<User> findAllByEmail(String email);

    List<User> findByEmailIn(List<String> emails);

    @Nonnull
    Page<User> findAll(Specification<User>spec, Pageable pageable);

    /**
     * 사용자 목록 삭제
     *
     * @param emails 삭제할 사용자의 이메일 목록
     */
    void deleteByEmailIn(List<String> emails);

    /**
     * 사용자 정보 일괄 삭제
     * @param email
     */
    void deleteAllByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByEmail(String email);

    Optional<User> findOneByPwResetKey(String resetKey);

}
