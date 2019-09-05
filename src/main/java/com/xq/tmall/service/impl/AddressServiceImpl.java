package com.xq.tmall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xq.tmall.dao.AddressMapper;
import com.xq.tmall.entity.Address;
import com.xq.tmall.service.AddressService;

@Service("addressService")
public class AddressServiceImpl implements AddressService{

	 private AddressMapper addressMapper;
	  @Resource(name = "addressMapper")
	    public void setAddressMapper(AddressMapper addressMapper) {
	        this.addressMapper = addressMapper;
	    }
	 
	@Override
	public List<Address> getList(String address_name, String address_regionId) {
		return addressMapper.select(address_name, address_regionId);
	}

	@Override
	public Address get(String address_areaId) {
		return addressMapper.selectOne(address_areaId);
	}

	@Override
	public List<Address> getRoot() {
		return addressMapper.selectRoot();
	}

}
