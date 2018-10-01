package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ApiModel(description = "医院信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_HOSPITAL")
public class Hospital {

    @ApiModelProperty(notes = "医院ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "医院名称")
    @Column(name = "HOSPITAL_NAME", length = 128)
    private String hospitalName;

    @ApiModelProperty(hidden = true)
    @Transient
    private String departmentNames;

}
