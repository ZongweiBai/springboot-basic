package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 广告
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "T_ADVERTISEMENT")
public class Advertisement implements Serializable {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;
    /**
     * 广告标题
     */
    private String title;
    /**
     * 关联的类型 1:商品包 2：活动介绍页
     */
    private Integer relateType;
    /**
     * 关联的商品包ID
     */
    private Integer relateId;
    /**
     * 图片URL
     */
    private String bannerImg;
    /**
     * 排序
     */
    private Integer sortNum;
    /**
     * 开始时间
     */
    private Date startDate;
    /**
     * 结束时间
     */
    private Date endDate;
    /**
     * 图片链接（点击图片要打开的URL）
     */
    private String clickURL;
    /**
     * 0：表示启动界面广告 1：表示首页轮播广告 2：表示浮窗广告
     */
    private int advertisementType;
    /**
     * 创建时间
     */
    private Date createTime;

}
