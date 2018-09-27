package com.baymin.springboot.store.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@ApiModel(description = "联系人信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_CONTACT")
public class Contact {

    @ApiModelProperty(notes = "联系人ID")
    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ApiModelProperty(notes = "用户ID")
    @Column(name = "USER_ID", length = 32)
    private String userId;

    @ApiModelProperty(notes = "我的角色 1:监护人  2：被监护人")
    @Column(name = "MY_ROLE", length = 2)
    private String myRole; // 1:监护人  2：被监护人

    @ApiModelProperty(notes = "联系人姓名")
    @Column(name = "CONTACT_NAME", length = 20)
    private String contactName;

    @ApiModelProperty(notes = "性别 M：男 F：女")
    @Column(name = "SEX", length = 2)
    private String sex;

    @ApiModelProperty(notes = "联系人手机号码")
    @Column(name = "CONTACT_NUMBER", length = 20)
    private String contactNumber;

    @ApiModelProperty(notes = "是否默认 T|F")
    @Column(name = "DEFAULT_FLAG", length = 2)
    private String defaultFlag;

    @ApiModelProperty(hidden = true)
    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;
}
