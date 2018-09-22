package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonStatus;
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
@Table(name = "T_CARE_PLAN")
public class CarePlan {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "TYPE_ID", length = 32)
    private String typeId;

    @Column(name = "CASE_ID", length = 32)
    private String caseId;

    @Column(name = "PLAN_DESC", length = 2048)
    private String planDesc;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STATUS")
    private CommonStatus status;

    @Transient
    private SysDict typeDict;

    @Transient
    private SysDict caseDict;

}
