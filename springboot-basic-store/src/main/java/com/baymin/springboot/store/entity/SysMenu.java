package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * Created by baymin on 2016/8/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "T_SYS_MENU")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 4192075544699716172L;

    @Id
    @Column(name = "MENU_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    private String menuName;

    private String menuUrl;

    private Integer level;  //菜单等级 1：主菜单 2：二级菜单

    private Integer parentId;

    private String parentName;

    private Integer status; //菜单状态 1：可用 2：不可用 3：删除

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Long createTime;

    private String createBy;

    private String menuIcon;    // 菜单图标

    @Transient
    private List<SysMenu> subMenuList;

    @Transient
    private Boolean checked = false;

}
