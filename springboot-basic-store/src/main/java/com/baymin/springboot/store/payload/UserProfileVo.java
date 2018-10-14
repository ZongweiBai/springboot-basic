package com.baymin.springboot.store.payload;

import com.baymin.springboot.store.entity.UserProfile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@ApiModel(description = "用户详细信息")
@Data
@NoArgsConstructor
public class UserProfileVo {

    @ApiModelProperty(notes = "用户ID")
    private String id;

    @ApiModelProperty(notes = "用户手机")
    private String account;

    @ApiModelProperty(notes = "性别 M:男 F:女")
    private String sex; // M:男 F:女

    @ApiModelProperty(notes = "头像")
    private String imgUrl;

    @ApiModelProperty(notes = "昵称")
    private String nickName;

    public UserProfileVo(UserProfile userProfile) {
        if (Objects.isNull(userProfile)) {
            return;
        }
        this.id = userProfile.getId();
        this.account = userProfile.getAccount();
        this.sex = userProfile.getSex();
        this.nickName = userProfile.getNickName();
        this.imgUrl = userProfile.getImgUrl();
    }

}
