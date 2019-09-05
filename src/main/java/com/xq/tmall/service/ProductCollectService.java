package com.xq.tmall.service;

import com.xq.tmall.entity.ProductCollect;
import com.xq.tmall.util.PageUtil;

import java.util.List;


public interface ProductCollectService {
    Integer getTotal(ProductCollect productCollect);

    boolean colleanOne(ProductCollect productCollect);

    boolean offcolleanOne(ProductCollect productCollect);

    List<ProductCollect> getList(Integer user_Id, PageUtil pageUtil);

    ProductCollect getCollect(Integer productcollect_id);

}
