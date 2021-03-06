package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.BasicItem;
import com.baymin.springboot.store.enumconstant.BasicItemType;
import lombok.Data;

import java.util.Objects;

@Data
public class BasicItemVo {

    private String itemId;

    private BasicItemType basicItemType;

    private String itemName;

    private Double itemFee;

    private String description;

    private Boolean checked = false;

    public BasicItemVo(BasicItem basicItem) {
        if (Objects.isNull(basicItem)) {
            return;
        }
        this.itemId = basicItem.getId();
        this.basicItemType = basicItem.getBasicItemType();
        this.itemName = basicItem.getItemName();
        this.itemFee = basicItem.getItemFee();
        this.description = basicItem.getDescription();
        this.checked = basicItem.getChecked();
    }

}
