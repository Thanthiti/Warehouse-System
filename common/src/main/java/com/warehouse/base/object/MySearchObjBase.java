package com.warehouse.base.object;

import lombok.*;
import java.util.List;

@Getter
@Setter
public class MySearchObjBase {
    private int pageNo = 1;
    private int pageSize = 15;
    private int totalPage;
    private int totalRow;
    private int[] rowsPerPageOption = {15,30,50,100};
    private String sortField;
    private boolean queryCountFlag = true;

    private List resultList;
}
