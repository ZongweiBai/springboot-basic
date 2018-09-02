package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色表
 * Created by baymin on 2016/8/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SYS_ROLE")
public class SysRole implements Serializable {

    private static final long serialVersionUID = -127270392034689229L;

    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleType;

    private String roleName;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Long createTime;

    private String createBy;

    @Transient
    private List<SysMenu> menuList;
}
