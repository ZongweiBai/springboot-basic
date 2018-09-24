package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 意见反馈
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_FEEDBACK")
public class Feedback implements Serializable {

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "GRADE")
    private Integer grade = 5;

    @Column(name = "CONTENT", length = 2048)
    private String content;

    @Column(name = "USER_ID")
    private String userId;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
