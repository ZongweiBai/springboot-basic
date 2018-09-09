package com.baymin.springboot.store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ebaizon on 7/31/2017.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_USER_PROFILE", uniqueConstraints = {@UniqueConstraint(columnNames = "ACCOUNT")})
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 5349710843525977410L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ACCOUNT", nullable = false, unique = true, updatable = false, length = 32)
    private String account;

    @Column(name = "SEX", length = 2, nullable = false)
    private String sex; // M:男 F:女

    @Column(name = "NICK_NAME", length = 32)
    private String nickName;

    @Column(name = "IDP_NICK_NAME", length = 32)
    private String idpNickName;

    @Column(name = "IDP_ID", length = 64)
    private String idpId;

    @Column(name = "ACTUAL_NAME", length = 32)
    private String actualName;

    @Column(name = "PASSWORD", nullable = false, length = 32)
    private String password;

    @Column(name = "PAY_PASSWORD", length = 32)
    private String payPassword;

    @Column(name = "REGISTER_TIME", columnDefinition = "timestamp")
    private Date registerTime;

    @Column(name = "ORDER_COUNT", precision = 4, scale = 0)
    private Integer orderCount;
}
