package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.payload.SubCarePlanVo;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@ApiModel(description = "订单照护计划")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_CARE_PLAN")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class OrderCarePlan {

    @ApiModelProperty(notes = "照护计划ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "订单ID")
    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @ApiModelProperty(notes = "类型ID")
    @Column(name = "TYPE_ID", length = 32)
    private String typeId;

    @ApiModelProperty(notes = "适应症ID")
    @Column(name = "CASE_ID", length = 32)
    private String caseId;

    @ApiModelProperty(notes = "陪护计划子项明细")
    @Type(type = "json")
    @Column(name = "SUB_CARE_PLANS", columnDefinition = "json")
    private List<SubCarePlanVo> subCarePlans;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(notes = "护士/护工ID")
    @Column(name = "STAFF_ID", length = 32)
    private String staffId;

}
