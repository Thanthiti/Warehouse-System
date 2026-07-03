package com.warehouse.base.object;

import lombok.*;
import java.util.Date;

@Getter
@Setter
public class MyObjBase {
    private String id;
    private Date createDate;
    private String createBy;
    private Date updateDate;
    private String updateBy;
    private String statusCode;
}
