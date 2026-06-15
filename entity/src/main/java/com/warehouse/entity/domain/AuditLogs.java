package com.warehouse.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.Date;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogs {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "action", nullable = false, length = 100)
    private String action;

    @Column(name = "ref_id")
    private Long refId;

    @Nationalized
    @Column(name = "ACTION_DATE", nullable = false)
    private Date actionDate;

    @Nationalized
    @Column(name = "ACTION_BY", nullable = false, length = 50)
    private String actionBy;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Nationalized
    @Column(name = "CREATE_BY", length = 50)
    private String createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Nationalized
    @Column(name = "UPDATE_BY", length = 50)
    private String updateBy;

}