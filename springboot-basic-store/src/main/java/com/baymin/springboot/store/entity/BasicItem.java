package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * 基础项目费用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_BASIC_ITEM")
public class BasicItem {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "BASIC_ITEM_TYPE")
    private BasicItemType basicItemType;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STATUS")
    private CommonStatus status;

    @Column(name = "ITEM_NAME", length = 64)
    private String itemName;

    @Column(name = "ITEM_FEE", precision = 10, scale = 2)
    private Double itemFee;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "DESCRIPTION", length = 2048)
    private String description;

    @Transient
    private Boolean checked = false;
}
