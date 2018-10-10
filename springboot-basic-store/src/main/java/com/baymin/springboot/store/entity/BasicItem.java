package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.BasicItemType;
import com.baymin.springboot.store.enumconstant.CommonStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础项目费用
 */
@ApiModel(description = "基础项目")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_BASIC_ITEM")
public class BasicItem implements Serializable {

    private static final long serialVersionUID = -3793562407884366461L;

    @ApiModelProperty(notes = "基础项目ID", required = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "基础收费项目类型", required = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "BASIC_ITEM_TYPE")
    private BasicItemType basicItemType;

    @ApiModelProperty(hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STATUS")
    private CommonStatus status;

    @ApiModelProperty(notes = "基础项目名称")
    @Column(name = "ITEM_NAME", length = 64)
    private String itemName;

    @ApiModelProperty(notes = "基础项目费用")
    @Column(name = "ITEM_FEE", precision = 10, scale = 2)
    private Double itemFee;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "DESCRIPTION", length = 2048)
    private String description;

    @Transient
    private Boolean checked = false;
}
