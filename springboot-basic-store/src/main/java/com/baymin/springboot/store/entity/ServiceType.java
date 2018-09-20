package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
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
@Table(name = "T_SERVICE_TYPE")
public class ServiceType {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "SERVICE_NAME", length = 32)
    private String serviceName;

    @Column(name = "SERVICE_ICON", length = 128)
    private String serviceIcon;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @Column(name = "KEYWORD", length = 32)
    private String keyWord;

    @Column(name = "SERVICE_DESC", length = 256)
    private String serviceDesc;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
