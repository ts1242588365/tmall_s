package com.xq.tmall.service;

import com.xq.tmall.entity.PropertyValue;
import com.xq.tmall.util.PageUtil;

import java.util.List;

public interface PropertyValueService {
    boolean add(PropertyValue propertyValue);
    //添加
    boolean addList(List<PropertyValue> propertyValueList);
    //更新产品属性
    boolean update(PropertyValue propertyValue);
    //删除产品属性
    boolean deleteList(Integer[] propertyValue_id_list);
    //查询列表
    List<PropertyValue> getList(PropertyValue propertyValue, PageUtil pageUtil);
    PropertyValue get(Integer propertyValue_id);
    Integer getTotal(PropertyValue propertyValue);
}
