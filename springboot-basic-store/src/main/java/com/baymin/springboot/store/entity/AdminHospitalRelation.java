package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ADMIN_HOSPITAL_RELATION")
public class AdminHospitalRelation implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "管理员ID")
    @Column(name = "ADMIN_ID", length = 32)
    private String adminId;

    @ApiModelProperty(notes = "医院ID")
    @Column(name = "HOSPITAL_ID", length = 32)
    private String hospitalId;

}
