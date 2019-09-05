package com.xq.tmall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xq.tmall.dao.ReviewMapper;
import com.xq.tmall.entity.Review;
import com.xq.tmall.service.ReviewService;
import com.xq.tmall.util.PageUtil;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService{
private ReviewMapper reviewMapper;
		@Resource(name="reviewMapper")
		
		public void setReviewMapper(ReviewMapper reviewMapper) {
			//获取当前的mapper对象
			this.reviewMapper=reviewMapper;
		}
		
		
		//添加一个命名空间
		@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	
		//添加评论
		@Override
	public boolean add(Review review) {
		// TODO Auto-generated method stub
		return reviewMapper.insertOne(review)>0;
	}

		//添加一个命名空间
		@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
		//更新添加功能	
	@Override
	public boolean update(Review review) {
		// TODO Auto-generated method stub
		return reviewMapper.updateOne(review)>0;
	}

		//添加一个命名空间
		@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
		//用ID来看所有数据
	@Override
	public boolean deleteList(Integer[] review_id_list) {
		// TODO Auto-generated method stub
		return reviewMapper.deleteList(review_id_list)>0;
	}
		//显示整条数据
	@Override
	public List<Review> getList(Review review, PageUtil pageUtil) {
		// TODO Auto-generated method stub
		return reviewMapper.select(review, pageUtil);
	}
	//显示整条数据并找到该用户
	@Override
	public List<Review> getListByUserId(Integer user_id, PageUtil pageUtil) {
		// TODO Auto-generated method stub
		return reviewMapper.selectByUserId(user_id, pageUtil);
	}
	
	@Override
	public List<Review> getListByProductId(Integer product_id, PageUtil pageUtil) {
		// TODO Auto-generated method stub
		return reviewMapper.selectByProductId(product_id, pageUtil);
	}
		
	@Override
	public Review get(Integer review_id) {
		// TODO Auto-generated method stub
		return reviewMapper.selectOne(review_id);
	}
	  
	@Override
	public Integer getTotal(Review review) {
		// TODO Auto-generated method stub
		return reviewMapper.selectTotal(review);
	}
		
	@Override
	public Integer getTotalByUserId(Integer user_id) {
		// TODO Auto-generated method stub
		return reviewMapper.selectTotalByUserId(user_id);
	}

	@Override
	public Integer getTotalByProductId(Integer product_id) {
		// TODO Auto-generated method stub
		return reviewMapper.selectTotalByProductId(product_id);
	}

	@Override
	public Integer getTotalByOrderItemId(Integer productOrderItem_id) {
		// TODO Auto-generated method stub
		return reviewMapper.selectTotalByOrderItemId(productOrderItem_id);
	}
}
