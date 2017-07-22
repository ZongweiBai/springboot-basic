package com.baymin.springboot.persistence.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Baymin on 2017/7/18.
 */
@Data
@NoArgsConstructor
@Embeddable
public class TeacherStudentRelPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "TEACHER_ID")
    private Teacher teacher;

}
