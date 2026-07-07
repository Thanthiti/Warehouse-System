package com.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String productName;
    private String sku;
    private Integer quantity;
    private String statusCode;
    private Date createDate;
    private Date updateDate;
}
