package com.warehouse.entity.objSearch;

import com.warehouse.base.object.MySearchObjBase;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductSearchObj extends MySearchObjBase {
    private String productName;
    private String sku;
    private String statusCode;
}
