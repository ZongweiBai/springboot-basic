package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_USER_WALLET")
public class UserWallet {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "USER_ID", length = 32)
    private String userId;

    @ApiModelProperty(notes = "用户类型 U:普通用户 S:护士/护工")
    @Column(name = "USER_TYPE", length = 5)
    private String userType;

    @ApiModelProperty(notes = "总收入")
    @Column(name = "TOTAL_INCOME", precision = 10, scale = 2)
    private Double totalIncome;

    @ApiModelProperty(notes = "当前余额")
    @Column(name = "BALANCE", precision = 10, scale = 2)
    private Double balance;

    @ApiModelProperty(notes = "总提现")
    @Column(name = "TOTAL_WITHDRAW", precision = 10, scale = 2)
    private Double totalWithdraw;

    @ApiModelProperty(notes = "提现中")
    @Column(name = "IN_WITHDRAWING", precision = 10, scale = 2)
    private Double totalInWithdrawing;

}
