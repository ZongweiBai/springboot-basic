package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.BasicServiceType;
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
@Table(name = "T_BASIC_SERVICE_FEE")
public class BasicServiceFee {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "BASIC_SERVICE_TYPE")
    private BasicServiceType basicServiceType;

    @Column(name = "SERVICE_NAME", length = 64)
    private String serviceName;

    @Column(name = "SERVICE_FEE", precision = 10, scale = 2)
    private Double serviceFee;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "DESCRIPTION", length = 2048)
    private String description;
}
