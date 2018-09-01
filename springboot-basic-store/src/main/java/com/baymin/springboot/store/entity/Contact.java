package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 联系人信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "T_CONTACT")
public class Contact {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "USER_ID", length = 32)
    private String userId;

    @Column(name = "MY_ROLE", length = 2)
    private String myRole; // 1:监护人  2：被监护人

    @Column(name = "CONTACT_NAME", length = 20)
    private String contactName;

    @Column(name = "SEX", length = 2)
    private String sex;

    @Column(name = "CONTACT_NUMBER", length = 20)
    private String contactNumber;

    @Column(name = "DEFAULT_CONTACT", length = 2)
    private String defaultContact;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;
}
