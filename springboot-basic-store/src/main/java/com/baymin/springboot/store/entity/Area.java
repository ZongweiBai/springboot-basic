package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 地区表
 * Created by baymin on 2016/5/7.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_AREA")
public class Area implements Serializable {

    private static final long serialVersionUID = -6580874147592008046L;

    @Id
    @Column(nullable = false, unique = true, name = "AREA_ID", length = 32)
    private String areaId;

    @Column(nullable = false, name = "AREA_NAME", length = 64)
    private String areaName;

    @Column(nullable = false, name = "PARENT_ID", length = 32)
    private String parentId;

}
