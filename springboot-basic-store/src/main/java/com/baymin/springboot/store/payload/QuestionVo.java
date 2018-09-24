package com.baymin.springboot.store.payload;

import com.baymin.springboot.common.constant.Constant;
import com.baymin.springboot.store.entity.Question;
import com.baymin.springboot.store.enumconstant.CareType;
import lombok.Data;

import java.util.Objects;

@Data
public class QuestionVo {

    private String id;

    private CareType careType;

    private String questionType;

    private String itemName;

    private String itemDesc;

    private String itemIcon;

    private Boolean multiChoice;

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
        this.multiChoice = question.getMultiChoice();
    }

}
