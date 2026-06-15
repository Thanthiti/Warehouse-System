package com.warehouse.entity.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName;

    @Column(name = "sku", unique = true, nullable = false, length = 50)
    private String sku;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

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