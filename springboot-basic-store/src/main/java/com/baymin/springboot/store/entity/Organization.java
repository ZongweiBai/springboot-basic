package com.baymin.springboot.store.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "T_ORGANIZATION")
public class Organization {

    @Id
    @Column(name = "ID", length = 32)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "ORG_NAME", length = 32)
    private String orgName;

    @Column(name = "ORG_ICON", length = 256)
    private String orgIcon;

    @Column(name = "ORG_DESC", length = 1024)
    private String orgDesc;

    @Column(name = "CREATE_TIME", columnDefinition = "timestamp")
    private Date createTime;

}
