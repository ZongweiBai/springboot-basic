package com.baymin.springboot.store.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_WECHAT_USER_INFO")
public class WechatUserInfo {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "UNIONID")
    private String unionid;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "CITY")
    private String city;

    @Column(name = "OPENID")
    private String openid;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "HEADIMGURL")
    private String headimgurl;

}
