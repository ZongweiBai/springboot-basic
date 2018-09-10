package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonDealStatus;
import com.baymin.springboot.store.enumconstant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORDER_STAFF_CHANGE")
public class OrderStaffChange {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "DEAL_STATUS", length = 2)
    private CommonDealStatus dealStatus;

    @Column(name = "CHANGE_DESC", length = 1024)
    private String changeDesc;

    @Column(name = "OLD_STAFF_ID", length = 32)
    private String oldStaffId;

    @Column(name = "NEW_STAFF_ID", length = 32)
    private String newStaffId;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

}
