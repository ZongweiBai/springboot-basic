package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ROLE_TYPE", length = 4)
    private String roleType;

    @Column(name = "ROLE_NAME", length = 20)
    private String roleName;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Transient
    private List<SysMenu> menuList;
}
