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

    @Column(name = "MOBILE", length = 20, nullable = false)
    private String mobile;

    @Column(name = "USER_NAME", length = 20, nullable = false)
    private String userName;

    @Column(name = "SEX", length = 2)
    private String sex; // M:男 F:女

    @Column(name = "EXPERIENCE", length = 2, nullable = false)
    private Integer experience;

    @Column(name = "AGE", length = 4)
    private Integer age;

    @Column(name = "HEIGHT", precision = 5, scale = 2)
    private Integer height;

    @Column(name = "WEIGHT", precision = 5, scale = 2)
    private Integer weight;

    @Column(name = "NATIONALITY", length = 10)
    private String nationality;

    @Column(name = "BIRTHPLACE", length = 128)
    private String birthplace;

    @Column(name = "LOCALISM", length = 32)
    private String localism;

    @Column(name = "MANDARIN", length = 2)
    private String mandarin; // A\B\C

    @Column(name = "LOCATION", length = 128)
    private String localtion;

    @Column(name = "FIRST_SKILL", length = 20)
    private String firstSkill;

    @Column(name = "SPECIALTY", length = 256)
    private String specialty;

    @Column(name = "ID_CARD", length = 20, nullable = false)
    private String idCard;

    @Column(name = "HEALTH_CARD_ID", length = 256)
    private String healthCardId;

    @Column(name = "PENSION_CARD_ID", length = 256)
    private String pensionCardId;

    @Column(name = "HEALTH_CARE_CARD_ID", length = 256)
    private String healthCareCardId;

    @Column(name = "SERVICE_COUNT", precision = 4, scale = 0)
    private Integer serviceCount;

    @Column(name = "PERSONAL_PROFILE", length = 256)
    private String personalProfile;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_STAFF_TYPE", length = 2)
    private ServiceStaffType serviceStaffType;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_STATUS")
    private ServiceStatus serviceStatus;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STAFF_STATUS")
    private CommonStatus staffStatus;
}
