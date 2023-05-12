package com.yieon.practice.auth.lib.model.domain.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * ------------------------
 * summary : USER ACTIVATION DOMAIN
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
@Entity
@Table(name="USER_ACTIVATION")
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserActivation implements Serializable {

    @Id
    @Column(name="USER_ACTIVATION_ID", nullable = false)
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMAIL", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private ClientInfo clientInfo;

    @Type(type="yes_no")
    @Column(name = "ACTIVATED", nullable = false)
    private Boolean activated;
}
