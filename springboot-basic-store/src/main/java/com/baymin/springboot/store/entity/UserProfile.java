package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ebaizon on 7/31/2017.
 */
@ApiModel(description = "用户详细信息")
@Data
@NoArgsConstructor
@Entity
@Table(name = "T_USER_PROFILE")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 5349710843525977410L;

    @ApiModelProperty(notes = "用户ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "用户手机")
    @Column(name = "ACCOUNT", nullable = true, unique = false, length = 32)
    private String account;

    @ApiModelProperty(notes = "性别 M:男 F:女")
    @Column(name = "SEX", length = 2, nullable = true)
    private String sex; // M:男 F:女

    @ApiModelProperty(notes = "昵称")
    @Column(name = "NICK_NAME", length = 32)
    private String nickName;

    @Column(name = "IMG_URL", length = 1024)
    private String imgUrl;

    @ApiModelProperty(notes = "微信昵称", hidden = true)
    @Column(name = "IDP_NICK_NAME", length = 32)
    private String idpNickName;

    @ApiModelProperty(notes = "微信openid", hidden = true)
    @Column(name = "IDP_ID", length = 64)
    private String idpId;

    @ApiModelProperty(notes = "真实姓名")
    @Column(name = "ACTUAL_NAME", length = 32)
    private String actualName;

    @Column(name = "PASSWORD", length = 32)
    private String password;

    @Column(name = "PAY_PASSWORD", length = 32)
    private String payPassword;

    @Column(name = "REGISTER_TIME", columnDefinition = "timestamp")
    private Date registerTime;

    @Column(name = "LAST_LOGIN_TIME", columnDefinition = "timestamp")
    private Date lastLoginTime;

    @Column(name = "ORDER_COUNT", precision = 4, scale = 0)
    private Integer orderCount;

    @Column(name = "BIRTHDAY", columnDefinition = "timestamp")
    private Date birthday;

    @Transient
    private String birthdayStr;
}
