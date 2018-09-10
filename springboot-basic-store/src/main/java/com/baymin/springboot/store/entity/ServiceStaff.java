package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_SERVICE_STAFF", uniqueConstraints = {@UniqueConstraint(columnNames = {"MOBILE", "ID_CARD"})})
public class ServiceStaff {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "USER_NAME", length = 20, nullable = false)
    private String userName;

    @Column(name = "SEX", length = 2, nullable = false)
    private String sex; // M:男 F:女

    @Column(name = "AGE", length = 4, nullable = false)
    private Integer age;

    @Column(name = "MOBILE", length = 20, nullable = false)
    private String mobile;

    @Column(name = "SERVICE_COUNT", precision = 4, scale = 0)
    private Integer serviceCount;

    @Column(name = "ID_CARD", length = 20, nullable = false)
    private String idCard;

    @Column(name = "HEALTH_CARD_IMG", length = 256)
    private String healthCardImg;

    @Column(name = "EXPERIENCE", length = 2, nullable = false)
    private Integer experience;

    @Column(name = "PERSONAL_PROFILE", length = 256)
    private String personalProfile;

    @Column(name = "SPECIALTY", length = 256)
    private String specialty;

    @Column(name = "NATIONALITY", length = 10)
    private String nationality;

    @Column(name = "BIRTHPLACE", length = 128)
    private String birthplace;

    @Column(name = "LOCALISM", length = 32)
    private String localism;

    @Column(name = "MANDARIN", length = 2)
    private String mandarin; // A\B\C

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_STAFF_TYPE", length = 2)
    private ServiceStaffType serviceStaffType;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "SERVICE_STATUS")
    private ServiceStatus serviceStatus;

    @Column(name = "STAFF_STATUS")
    private CommonStatus staffStatus;
}
