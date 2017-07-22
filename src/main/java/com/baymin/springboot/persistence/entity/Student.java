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
@Table(name = "T_STUDENT", uniqueConstraints = {@UniqueConstraint(columnNames = "STUDENT_ID")})
public class Student {

    @Id
    @Column(name = "STUDENT_ID", length = 32)
    private String studentId;

    @Column(name = "STUDENT_NAME")
    private String studentName;

}
