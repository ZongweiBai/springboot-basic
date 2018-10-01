package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@ApiModel(description = "医院科室信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_HOSPITAL_DEPARTMENT")
public class HospitalDepartment {

    @ApiModelProperty(notes = "科室ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "医院ID")
    @Column(name = "HOSPITAL_ID", length = 32)
    private String hospitalId;

    @ApiModelProperty(notes = "科室名称")
    @Column(name = "DEPARTMENT_NAME", length = 128)
    private String departmentName;

}
