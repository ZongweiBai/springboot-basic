package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.IncomeType;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
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

    @Column(name = "INCOME_REMARK")
    private String incomeRemark;

    @Column(name = "CURRENT_BALANCE", precision=10, scale=2)
    private Double currentBalance;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "INCOME_TYPE", length = 2)
    private IncomeType incomeType;

}
