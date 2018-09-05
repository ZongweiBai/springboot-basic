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
 * 菜单
 * Created by baymin on 2016/8/4.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SYS_MENU")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 4192075544699716172L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "MENU_NAME", length = 32)
    private String menuName;

    @Column(name = "MENU_URL", length = 32)
    private String menuUrl;

    @Column(name = "LEVEL")
    private Integer level;  //菜单等级 1：主菜单 2：二级菜单

    @Column(name = "PARENT_ID", length = 32)
    private String parentId;

    @Column(name = "PARENT_NAME", length = 32)
    private String parentName;

    @Column(name = "STATUS")
    private Integer status; //菜单状态 1：可用 2：不可用 3：删除

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "MENU_ICON", length = 20)
    private String menuIcon;    // 菜单图标

    @Transient
    private List<SysMenu> subMenuList;

    @Transient
    private Boolean checked = false;

}
