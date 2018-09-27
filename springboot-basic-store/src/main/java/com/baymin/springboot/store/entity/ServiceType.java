package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "服务类别")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SERVICE_TYPE")
public class ServiceType {

    @ApiModelProperty(notes = "类别ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "服务类别名称")
    @Column(name = "SERVICE_NAME", length = 32)
    private String serviceName;

    @ApiModelProperty(notes = "服务类别图标")
    @Column(name = "SERVICE_ICON", length = 128)
    private String serviceIcon;

    @ApiModelProperty(notes = "服务类别枚举")
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @ApiModelProperty(hidden = true)
    @Column(name = "KEYWORD", length = 32)
    private String keyWord;

    @ApiModelProperty(notes = "服务类别描述")
    @Column(name = "SERVICE_DESC", length = 256)
    private String serviceDesc;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
