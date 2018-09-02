package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 被照护人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_CONTACT_CARED")
public class ContactCared {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "USER_ID", length = 32)
    private String userId;

    @Column(name = "CONTACT_NAME", length = 20)
    private String contactName;

    @Column(name = "SEX", length = 2)
    private String sex;

    @Column(name = "TALL", precision = 5, scale = 2)
    private Double tall;

    @Column(name = "WEIGHT", precision = 5, scale = 2)
    private Double weight;

    @Column(name = "CONTACT_NUMBER", length = 20)
    private String contactNumber;

    @Column(name = "OTHER_INFO", length = 256)
    private String otherInfo;

    @Column(name = "DEFAULT_FLAG", length = 2)
    private String defaultFlag;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;
}
