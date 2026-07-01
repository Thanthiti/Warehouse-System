package com.warehouse.base;
import com.warehouse.base.object.MyObjBase;
import com.warehouse.base.object.MySearchObjBase;

import java.util.List;

public interface MyBaseRepository {

    public MyObjBase add(MyObjBase param) throws Exception;

    public MyObjBase update(MyObjBase param) throws Exception;

    public MyObjBase delete(MyObjBase param) throws Exception;

    public MyObjBase findByID(MyObjBase param) throws Exception;

    public MySearchObjBase list(MySearchObjBase param) throws Exception;
}
