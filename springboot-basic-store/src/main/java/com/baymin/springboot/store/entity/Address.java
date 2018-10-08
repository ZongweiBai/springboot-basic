package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户地址
 */
@ApiModel(description = "地址")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADDRESS")
public class Address {

    @ApiModelProperty(notes = "地址ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "USER_ID", length = 32)
    private String userId;
    /**
     * 省
     */
    @ApiModelProperty(hidden = true)
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(hidden = true)
    private String city;

    /**
     * 地址类型 H：医院 M：居家
     */
    @ApiModelProperty(notes = "地址类型 H：医院 M：居家")
    @Column(name = "ADDRESS_TYPE", length = 2)
    private String addressType;

    /**
     * 医院
     */
    @ApiModelProperty(notes = "医院名称")
    @Column(name = "HOSPITAL", length = 50)
    private String hospital;

    /**
     * 科室
     */
    @ApiModelProperty(notes = "科室名称")
    @Column(name = "DEPARTMENT", length = 20)
    private String department;

    /**
     * 医院详情 2号楼3层 12床{"build":"XXX", "floor": "XXX", "bed", "XXX"}
     */
    @ApiModelProperty(notes = "院详情 2号楼3层 12床 例如：{\"build\":\"XXX\", \"floor\": \"XXX\", \"bed\", \"XXX\"}")
    @Column(name = "HOSPITAL_DETAIL", length = 128)
    private String hospitalDetail;

    /**
     * 社区
     */
    @ApiModelProperty(notes = "社区名称")
    @Column(name = "COMMUNITY", length = 50)
    private String community;

    /**
     * 社区详情 4号楼7单元1603室 {"build":"XXX", "unit": "XXX", "room", "XXX"}
     */
    @ApiModelProperty(notes = "社区详情 4号楼7单元1603室 例如： {\"build\":\"XXX\", \"unit\": \"XXX\", \"room\", \"XXX\"}")
    @Column(name = "COMMUNITY_DETAIL", length = 128)
    private String communityDetail;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    public Address(String addressId) {
        this.id = addressId;
    }
}
