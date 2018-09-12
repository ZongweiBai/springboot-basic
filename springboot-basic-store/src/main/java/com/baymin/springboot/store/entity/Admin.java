package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADMIN")
public class Admin implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    /**
     * 账号
     */
    @Column(name = "ACCOUNT", length = 32)
    private String account;
    /**
     * 密码
     */
    @Column(name = "PASSWORD", length = 32)
    private String password;
    /**
     * 等级
     * 1、超级管理员 2、普通管理员
     */
    @Column(name = "GRADE", precision = 2, scale = 0)
    private int grade;
    /**
     * 角色ID
     */
    @Column(name = "ROLE_ID", length = 32)
    private String roleId;
    /**
     * 角色名称
     */
    @Column(name = "ROLE_NAME", length = 32)
    private String roleName;
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "LAST_LOGIN_TIME", columnDefinition = "timestamp")
    private Date lastLoginTime;
    /**
     * 手机号码
     */
    @Column(name = "MOBILE", length = 11)
    private String mobile;

    @Column(name = "EMAIL", length = 32)
    private String email;

    @Column(name = "ORG_ID", length = 32)
    private String orgId;

    @Column(name = "ADMIN_NOTE", length = 256)
    private String adminNote;

}
