package com.baymin.springboot.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Baymin on 2017/4/9.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_USER_PROFILE", uniqueConstraints = {@UniqueConstraint(columnNames = "ACCOUNT")})
public class UserProfile implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ACCOUNT", nullable = false, unique = true, updatable = false, length = 32)
    private String account;

    @Column(name = "NICK_NAME", length = 32)
    private String nickName;

    @Column(name = "ACTUAL_NAME", length = 32)
    private String actualName;

    @Column(name = "PASSWORD", nullable = false, length = 32)
    private String password;

    @Column(name = "PAY_PASSWORD", length = 32)
    private String payPassword;

    @Column(name = "REGISTER_TIME", columnDefinition = "timestamp")
    private Date registerTime;
}
