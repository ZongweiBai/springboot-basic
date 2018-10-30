package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CommonStatus;
import com.baymin.springboot.store.enumconstant.ServiceStaffType;
import com.baymin.springboot.store.enumconstant.ServiceStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "护士/护工明细")
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_SERVICE_STAFF", uniqueConstraints = {@UniqueConstraint(columnNames = {"MOBILE", "ID_CARD"})})
public class ServiceStaff {

    @ApiModelProperty(notes = "主键ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "手机号码")
    @Column(name = "MOBILE", length = 20, nullable = false)
    private String mobile;

    @ApiModelProperty(notes = "姓名")
    @Column(name = "USER_NAME", length = 20, nullable = false)
    private String userName;

    @ApiModelProperty(notes = "头像")
    @Column(name = "IMG_URL", length = 1024)
    private String imgUrl;

    @ApiModelProperty(notes = "性别 M:男 F:女")
    @Column(name = "SEX", length = 2)
    private String sex; // M:男 F:女

    @ApiModelProperty(notes = "护理经验 单位：年")
    @Column(name = "EXPERIENCE", length = 2, nullable = false)
    private Integer experience;

    @ApiModelProperty(notes = "年龄")
    @Column(name = "AGE", length = 4)
    private Integer age;

    @ApiModelProperty(notes = "身高 单位：cm")
    @Column(name = "HEIGHT", precision = 5, scale = 2)
    private Integer height;

    @ApiModelProperty(notes = "体重 单位:kg")
    @Column(name = "WEIGHT", precision = 5, scale = 2)
    private Integer weight;

    @ApiModelProperty(notes = "民族")
    @Column(name = "NATIONALITY", length = 10)
    private String nationality;

    @ApiModelProperty(notes = "籍贯省份ID")
    @Column(name = "BIRTHPLACE_PID", length = 128)
    private String birthplacePid;

    @ApiModelProperty(notes = "籍贯城市ID")
    @Column(name = "BIRTHPLACE_CID", length = 128)
    private String birthplaceCid;

    @ApiModelProperty(notes = "方言")
    @Column(name = "LOCALISM", length = 32)
    private String localism;

    @ApiModelProperty(notes = "普通话")
    @Column(name = "MANDARIN", length = 20)
    private String mandarin; // A\B\C

    @ApiModelProperty(notes = "护理省份ID")
    @Column(name = "LOCATION_PID", length = 128)
    private String locationPid;

    @ApiModelProperty(notes = "护理城市ID")
    @Column(name = "LOCATION_CID", length = 128)
    private String locationCid;

    @ApiModelProperty(notes = "优先服务类型")
    @Column(name = "FIRST_SKILL", length = 20)
    private String firstSkill;

    @ApiModelProperty(notes = "护理特长")
    @Column(name = "SPECIALTY", length = 256)
    private String specialty;

    @ApiModelProperty(notes = "身份证")
    @Column(name = "ID_CARD", length = 20, nullable = false)
    private String idCard;

    @ApiModelProperty(notes = "健康证")
    @Column(name = "HEALTH_CARD_ID", length = 256)
    private String healthCardId;

    @ApiModelProperty(notes = "养老护理员职业资格证")
    @Column(name = "PENSION_CARD_ID", length = 256)
    private String pensionCardId;

    @ApiModelProperty(notes = "医疗照护职业资格证")
    @Column(name = "HEALTH_CARE_CARD_ID", length = 256)
    private String healthCareCardId;

    @ApiModelProperty(notes = "服务次数")
    @Column(name = "SERVICE_COUNT", precision = 4, scale = 0)
    private Integer serviceCount;

    @ApiModelProperty(notes = "主键ID", hidden = true)
    @Column(name = "PERSONAL_PROFILE", length = 256)
    private String personalProfile;

    @ApiModelProperty(notes = "护士/护工类型")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_STAFF_TYPE", length = 2)
    private ServiceStaffType serviceStaffType;

    @ApiModelProperty(notes = "创建时间", hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @ApiModelProperty(notes = "服务状态")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "SERVICE_STATUS")
    private ServiceStatus serviceStatus;

    @ApiModelProperty(notes = "当前服务订单数")
    @Column(name = "SERVICE_ORDER_COUNT", precision = 4, scale = 0)
    private Integer serviceOrderCount;

    @ApiModelProperty(notes = "状态", hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "STAFF_STATUS")
    private CommonStatus staffStatus;

    @ApiModelProperty(notes = "微信OpenId", hidden = true)
    @Column(name = "IDP_ID", length = 128)
    private String idpId;

    @ApiModelProperty(notes = "是否开启接单")
    @Column(name = "ASSIGN_ORDER_NOTIFICATION")
    private Boolean assignOrderNotification;

    @ApiModelProperty(notes = "籍贯")
    @Column(name = "BIRTHPLACE", length = 128)
    private String birthplace;

    @ApiModelProperty(notes = "护理城市")
    @Column(name = "LOCATION", length = 128)
    private String localtion;
}
