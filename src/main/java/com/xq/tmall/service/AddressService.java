package com.xq.tmall.service;

import java.util.List;

import com.xq.tmall.entity.Address;

public interface AddressService {
	//获取用户所在地区级地址
    Address get(String address_areaId);
    
    //获取其他地址信息
    List<Address> getRoot();
    
    List<Address> getList(String address_name, String address_regionId);
}
