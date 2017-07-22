package com.baymin.springboot.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by Baymin on 2017/7/18.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_TEACHER", uniqueConstraints = {@UniqueConstraint(columnNames = "TEACHER_ID")})
public class Teacher {

    @Id
    @Column(name = "TEACHER_ID", length = 32)
    private String teacherId;

    @Column(name = "TEACHER_NAME")
    private String teacherName;

}
