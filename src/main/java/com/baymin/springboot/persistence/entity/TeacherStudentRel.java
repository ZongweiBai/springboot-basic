package com.baymin.springboot.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Created by Baymin on 2017/7/18.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_TEACHER_STUDENT_REL", uniqueConstraints = {@UniqueConstraint(columnNames = "STUDENT_ID")})
public class TeacherStudentRel {

    @EmbeddedId
    private TeacherStudentRelPK teacherStudentRelPK;

}
