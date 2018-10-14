package com.baymin.springboot.store.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * 短信验证码发送记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_SMS_SEND_RECORD")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class SmsSendRecord {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "MOBILE", length = 20)
    private String mobile;

    @Column(name = "TEMPLATE_ID", length = 20)
    private String templateId;

    @Type(type = "json")
    @Column(name = "TEMPLATE_PARAMS", columnDefinition = "json")
    private Map<String, String> templateParams;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

    @Column(name = "SEND_TIME", columnDefinition = "timestamp")
    private Date sendTime;

    @Column(name = "SEND_RESULT")
    private Boolean sendResult;

    @Column(name = "RESULT_DESC", length = 256)
    private String resultDesc;

}
