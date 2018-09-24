package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_STAFF_INCOME")
public class StaffIncome {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "STAFF_ID", length = 32)
    private String staffId;

    @Column(name = "ORDER_ID", length = 32)
    private String orderId;

    @Column(name = "ORDER_TOTAL_FEE", precision=10, scale=2)
    private Double orderTotalFee;

    @Column(name = "INCOME", precision = 10, scale = 2)
    private Double income;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
