package com.baymin.springboot.store.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统字典表
 */
@Entity(name = "T_SYS_DICT")
public class SysDict implements Serializable {

    private static final long serialVersionUID = 3686269078770784906L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codeName;        // 字典名

    private String codeKey;         // 字典key

    private String codeValue;       // 字典值

    private Date createTime;        // 创建时间

    public SysDict() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName == null ? null : codeName.trim();
    }

    public String getCodeKey() {
        return codeKey;
    }

    public void setCodeKey(String codeKey) {
        this.codeKey = codeKey == null ? null : codeKey.trim();
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue == null ? null : codeValue.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}