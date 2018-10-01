package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "换人申请")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_STAFF_CHANGE")
public class OrderStaffChange {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "CHANGE_DESC", length = 1024)
    private String changeDesc;

    @ApiModelProperty(hidden = true)
    @Column(name = "OLD_STAFF_ID", length = 32)
    private String oldStaffId;

    @ApiModelProperty(hidden = true)
    @Column(name = "NEW_STAFF_ID", length = 32)
    private String newStaffId;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    @ApiModelProperty(hidden = true)
    @Column(name = "DEAL_DESC", length = 1024)
    private String dealDesc;

}
