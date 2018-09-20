package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 角色菜单关联表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_RELATE_ROLE_MENU")
public class RelateRoleMenu implements Serializable {

    private static final long serialVersionUID = -1580351871958057802L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ROLE_ID", length = 32)
    private String roleId;

    @Column(name = "MENU_ID", length = 32)
    private String menuId;

    public RelateRoleMenu(String menuId, String roleId) {
        this.menuId = menuId;
        this.roleId = roleId;
    }

}