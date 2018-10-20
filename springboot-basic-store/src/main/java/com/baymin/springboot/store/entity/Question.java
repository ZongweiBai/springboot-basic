package com.baymin.springboot.store.entity;

import com.baymin.springboot.store.enumconstant.CareType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "问题")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_QUESTION")
public class Question implements Serializable {

    private static final long serialVersionUID = 7395197720021454000L;

    @ApiModelProperty(hidden = true)
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(hidden = true)
    @Type(type = "com.baymin.springboot.store.enumconstant.convert.DbEnumType")
    @Column(name = "CARE_TYPE")
    private CareType careType;

    @ApiModelProperty(notes = "问题类型 DISEASES:问题1 SELF_CARE：问题2 EATING：问题3 CATHETER_CARE：问题4 ASSIST_WITH_MEDICATION：问题5")
    @Column(name = "QUESTION_TYPE", length = 50)
    private String questionType;

    @ApiModelProperty(notes = "问题名称")
    @Column(name = "ITEM_NAME", length = 20)
    private String itemName;

    @ApiModelProperty(notes = "问题详情")
    @Column(name = "ITEM_DESC", length = 100)
    private String itemDesc;

    @ApiModelProperty(hidden = true)
    @Column(name = "ITEM_ICON", length = 256)
    private String itemIcon;

    @ApiModelProperty(hidden = true)
    @Column(name = "ITEM_ICON_SELECTED", length = 256)
    private String itemIconSelected;

    @ApiModelProperty(hidden = true)
    @Column(name = "MULTI_CHOICE")
    private Boolean multiChoice = false;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
