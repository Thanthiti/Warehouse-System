package com.warehouse.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
public class Users  {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "email", unique = true, nullable = false, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @Column(name = "STATUS_CODE", nullable = false, length = 10)
    private String statusCode;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY", length = 50)
    private String createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY", length = 50)
    private String updateBy;

}