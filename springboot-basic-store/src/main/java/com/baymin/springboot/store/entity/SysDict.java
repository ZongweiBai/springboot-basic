package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 系统字典表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SYS_DICT")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 3686269078770784906L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codeName;        // 字典名

    private String codeKey;         // 字典key

    private String codeValue;       // 字典值

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Long createTime;        // 创建时间

}