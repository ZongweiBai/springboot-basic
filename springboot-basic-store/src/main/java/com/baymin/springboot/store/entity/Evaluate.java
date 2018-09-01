package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单评价
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "T_EVALUATE")
public class Evaluate implements Serializable {

    private static final long serialVersionUID = 3109228357668258013L;

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Column(name = "GRADE")
    private Integer grade = 5;

    @Column(name = "DESCRIPTION", length = 2048)
    private String description;

    @Column(name = "CREATE_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "USER_ID", length = 32)
    private String userId;

}
