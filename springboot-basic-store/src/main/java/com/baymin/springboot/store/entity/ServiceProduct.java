package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SERVICE_PRODUCT")
public class ServiceProduct {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "PRODUCT_NAME", length = 64)
    private String productName;

    @Column(name = "PRODUCT_ICON", length = 128)
    private String productIcon;

    @Column(name = "SERVICE_TYPE_ID", length = 32)
    private String serviceTypeId;

    @Column(name = "BASIC_ITEMS", length = 2048)
    private String basicItems;

    @Column(name = "PRODUCT_PRICE", precision = 10, scale = 2)
    private Double productPrice;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "PRODUCT_STATUS")
    private CommonStatus productStatus;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "SERVICE_TIPS", length = 2048)
    private String serviceTips;

    @Column(name = "APPOINTMENT_NOTICE", length = 2048)
    private String appointmentNotice;

    @Transient
    private List<BasicItem> itemList;

}
