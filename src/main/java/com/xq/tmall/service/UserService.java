package com.xq.tmall.service;

import java.util.List;

import com.xq.tmall.entity.User;
import com.xq.tmall.util.OrderUtil;
import com.xq.tmall.util.PageUtil;

public interface UserService {

	//获取用户ID
	 User get(Integer user_id);
	 //用户登录
	 User login(String user_name, String user_password);
	 
	 List<User> getList(User user, OrderUtil orderUtil, PageUtil pageUtil);
	 
	 Integer getTotal(User user);
	 
	 //修改
     boolean update(User user);

   //����
     boolean add(User user);

}
