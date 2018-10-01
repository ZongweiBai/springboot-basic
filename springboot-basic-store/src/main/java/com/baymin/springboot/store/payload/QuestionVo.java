package com.baymin.springboot.store.payload;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@ApiModel(description = "问题信息")
@Data
public class QuestionVo {

    @ApiModelProperty(notes = "问题ID")
    private String id;

    @ApiModelProperty(notes = "陪护类型")
    private CareType careType;

    @ApiModelProperty(notes = "问题类型 DISEASES:问题1 SELF_CARE：问题2 EATING：问题3 CATHETER_CARE：问题4 ASSIST_WITH_MEDICATION：问题5")
    private String questionType;

    @ApiModelProperty(notes = "问题名称")
    private String itemName;

    @ApiModelProperty(notes = "问题详情")
    private String itemDesc;

    @ApiModelProperty(notes = "问题图标:未选中")
    private String itemIcon;

    @ApiModelProperty(notes = "问题图标:已选中")
    private String itemIconSelected;
//
//    @ApiModelProperty(notes = "是否多选", hidden = true)
//    private Boolean multiChoice;

    public QuestionVo(Question question) {
        if (Objects.isNull(question)) {
            return;
        }
        this.id = question.getId();
        this.careType = question.getCareType();
        this.questionType = question.getQuestionType();
        this.itemName = question.getItemName();
        this.itemDesc = question.getItemDesc();
        this.itemIcon = Constant.IMG_HOST + question.getItemIcon();
        this.itemIconSelected = Constant.IMG_HOST + question.getItemIconSelected();
//        this.multiChoice = question.getMultiChoice();
    }

}
