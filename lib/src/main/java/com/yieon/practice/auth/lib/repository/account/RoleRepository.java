package com.yieon.practice.auth.lib.repository.account;

import com.yieon.practice.auth.lib.model.domain.account.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-14
 * <PRE>
 * ------------------------
 * summary : Role jpa Repository
 * ------------------------
 * Revision history
 * 2023. 04. 14. yieon : Initial creation
 * </PRE>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {

    /**
     * 권한그룹 코드 목록으로 권한그룹 목록 조회
     *
     * @param roleCds 권한그룹 코드 목록
     * @return
     */
    List<Role> findRolesByRoleCdIn(List<String> roleCds);

    /**
     * 권한그룹 일괄 삭제
     *
     * @param roleCds 일괄 삭제할 권한그룹 코드 목록
     */
    void deleteByRoleCdIn(List<String> roleCds);

    /**
     * 권한그룹 코드로 권한그룹 목록 조회
     *
     * @param roleCd 권한그룹 코드
     */
    List<Role> findAllByRoleCd(String roleCd);
}
