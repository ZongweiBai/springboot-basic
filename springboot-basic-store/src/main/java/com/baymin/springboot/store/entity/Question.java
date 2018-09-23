package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_QUESTION")
public class Question {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @Column(name = "QUESTION_TYPE", length = 20)
    private String questionType;

    @Column(name = "ITEM_NAME", length = 20)
    private String itemName;

    @Column(name = "ITEM_DESC", length = 100)
    private String itemDesc;

    @Column(name = "ITEM_ICON", length = 256)
    private String itemIcon;

    @Column(name = "MULTI_CHOICE")
    private Boolean multiChoice = false;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
