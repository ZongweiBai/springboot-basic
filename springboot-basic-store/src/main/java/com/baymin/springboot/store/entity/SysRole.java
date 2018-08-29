package com.baymin.springboot.store.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色表
 * Created by baymin on 2016/8/4.
 */
@Entity(name = "T_SYS_ROLE")
public class SysRole implements Serializable {

    private static final long serialVersionUID = -127270392034689229L;

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    private String roleType;

    private String roleName;

    private Date createTime;

    private String createBy;

    @Transient
    private List<SysMenu> menuList;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<SysMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<SysMenu> menuList) {
        this.menuList = menuList;
    }
}
