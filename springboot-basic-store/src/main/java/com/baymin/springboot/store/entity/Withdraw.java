package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.WithdrawResult;
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
 * 提现申请
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_WITHDRAW")
public class Withdraw implements Serializable {

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
    /**
     * 提现金额
     */
    @Column(name = "WITHDRAW_AMOUNT", precision = 10, scale = 2)
    private Double withdrawAmount;
    /**
     * 提现结果
     */
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "RESULT")
    private WithdrawResult result;
    @ApiModelProperty(hidden = true, notes = "创建时间")
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(hidden = true, notes = "处理时间")
    @Column(name = "DEAL_TIME", columnDefinition = "timestamp")
    private Date dealTime;

    /********************bank info************************/
    @ApiModelProperty(notes = "银行账户")
    @Column(name = "BANK_ACCOUNT_USER_NAME", length = 20)
    private String bankAccountUserName;

    @ApiModelProperty(notes = "银行账号")
    @Column(name = "BANK_ACCOUNT_NUMBER", length = 32)
    private String bankAccountNumber;

    @ApiModelProperty(notes = "开户行名称")
    @Column(name = "BANK_NAME", length = 64)
    private String bankName;

}
