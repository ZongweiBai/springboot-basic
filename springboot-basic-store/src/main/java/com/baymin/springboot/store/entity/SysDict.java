package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "DICT_NAME", length = 20)
    private String dictName;        // 字典名

    @ApiModelProperty(hidden = true)
    @Column(name = "CODE_KEY", length = 20)
    private String codeKey;         // 字典key

    @Column(name = "CODE_VALUE", length = 20)
    private String codeValue;       // 字典值

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;        // 创建时间

}