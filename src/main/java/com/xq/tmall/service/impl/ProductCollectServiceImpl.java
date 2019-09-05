package com.xq.tmall.service.impl;

import com.xq.tmall.dao.ProductCollectMapper;
import com.xq.tmall.entity.ProductCollect;
import com.xq.tmall.service.ProductCollectService;
import com.xq.tmall.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("productCollectService")
public class ProductCollectServiceImpl implements ProductCollectService {
	
    private ProductCollectMapper collectMapper;
    @Resource(name = "productCollectMapper")
    public void setCollectMapper(ProductCollectMapper collectMapper) {
        this.collectMapper = collectMapper;
    }

    @Override
    public Integer getTotal(ProductCollect productCollect) {
        return collectMapper.selectTotal(productCollect);
    }

    @Override
    public boolean colleanOne(ProductCollect productCollect) {
        return collectMapper.insertOne(productCollect)>0;
    }

    @Override
    public boolean offcolleanOne(ProductCollect productCollect) {
        return collectMapper.deleteOne(productCollect)>0;
    }

    @Override
    public List<ProductCollect> getList(Integer user_Id, PageUtil pageUtil) {
        return collectMapper.selectList(user_Id,pageUtil);
    }
    @Override
    public ProductCollect getCollect(Integer productcollect_id) {
        return collectMapper.selectOne(productcollect_id);
    }
}
