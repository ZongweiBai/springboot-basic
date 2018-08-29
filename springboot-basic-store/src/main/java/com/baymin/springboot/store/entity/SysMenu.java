package com.baymin.springboot.store.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * Created by baymin on 2016/8/4.
 */
@Entity(name = "T_SYS_MENU")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 4192075544699716172L;

    @Id
    @Column(name = "menu_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    private String menuName;

    private String menuUrl;

    private Integer level;  //菜单等级 1：主菜单 2：二级菜单

    private Integer parentId;

    private String parentName;

    private Integer status; //菜单状态 1：可用 2：不可用 3：删除

    private Date createTime;

    private String createBy;

    private String menuIcon;    // 菜单图标

    @Transient
    private List<SysMenu> subMenuList;

    @Transient
    private Boolean checked = false;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public List<SysMenu> getSubMenuList() {
        return subMenuList;
    }

    public void setSubMenuList(List<SysMenu> subMenuList) {
        this.subMenuList = subMenuList;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
