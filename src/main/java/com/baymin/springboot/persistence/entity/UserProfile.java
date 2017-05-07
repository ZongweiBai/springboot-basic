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

    private String id;
    private String account;
    private String nickName;
    private String actualName;
    private String password;
    private String payPassword;
    private Date registerTime;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    public String getId() {
        return id;
    }

    @Column(name = "ACCOUNT", nullable = false, unique = true, updatable = false, length = 32)
    public String getAccount() {
        return account;
    }

    @Column(name = "NICK_NAME", length = 32)
    public String getNickName() {
        return nickName;
    }

    @Column(name = "ACTUAL_NAME", length = 32)
    public String getActualName() {
        return actualName;
    }

    @Column(name = "PASSWORD", nullable = false, length = 32)
    public String getPassword() {
        return password;
    }

    @Column(name = "PAY_PASSWORD", length = 32)
    public String getPayPassword() {
        return payPassword;
    }

    @Column(name = "REGISTER_TIME", columnDefinition = "timestamp")
    public Date getRegisterTime() {
        return registerTime;
    }
}
