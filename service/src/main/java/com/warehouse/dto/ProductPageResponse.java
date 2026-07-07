package com.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductPageResponse {
    private List<ProductResponse> items;
    private int pageNo;
    private int pageSize;
    private int totalRow;
    private int totalPage;
}
