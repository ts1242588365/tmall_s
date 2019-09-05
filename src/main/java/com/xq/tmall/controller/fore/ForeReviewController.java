package com.xq.tmall.controller.fore;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Product;
import com.xq.tmall.entity.ProductOrder;
import com.xq.tmall.entity.ProductOrderItem;
import com.xq.tmall.entity.Review;
import com.xq.tmall.entity.User;
import com.xq.tmall.service.ProductImageService;
import com.xq.tmall.service.ProductOrderItemService;
import com.xq.tmall.service.ProductOrderService;
import com.xq.tmall.service.ProductService;
import com.xq.tmall.service.ReviewService;
import com.xq.tmall.service.UserService;
import com.xq.tmall.util.PageUtil;

@Controller
public class ForeReviewController extends BaseController {
	//实现依赖注入
	@Resource(name="reviewService")
	private ReviewService reviewService;//评论的service
	
	//实现依赖注入
	@Resource(name="userService")
	private UserService userService;
	
	//实现依赖注入
	@Resource(name="productOrderItemService")
	private ProductOrderItemService productOrderItemService;//商品订单项信息的service
	
	//实现依赖注入
	@Resource(name="productOrderService")
	private ProductOrderService productOrderService;//客户的订单地址service
	
	//实现依赖注入
	@Resource(name="productService")
	private ProductService productService;//商品的全部信息service
	
	//实现依赖注入
	@Resource(name="productImageService")
	private ProductImageService productImageService;//各个产品的service
	
	//转到前台天猫-评论添加页
	@RequestMapping(value="review/{orderItem_id}",method = RequestMethod.GET)//请求的方法类型
	public String goToPage(HttpSession session,Map<String, Object> map,@PathVariable("orderItem_id") Integer orderItem_id) {
		logger.info("检查用户是否登录");
		Object userId=checkUser(session);
		User user;
		if (userId!=null) {
			logger.info("获取用户信息");
			user=userService.get(Integer.parseInt(userId.toString()));
			map.put("user", user);
		}else {
			return "redirect:/login";
		}
		logger.info("获取订单项信息");
		ProductOrderItem orderItem=productOrderItemService.get(orderItem_id);
		if (orderItem==null) {
			logger.warn("订单项不存在，返回订单页");
			return "redirect:/order/0/10";
		}
		if (!orderItem.getProductOrderItem_user().getUser_id().equals(userId)) {
			logger.warn("订单项与用户不匹配，返回订单页");
			return "redirect:/order/0/10";
		}
		if (orderItem.getProductOrderItem_order()==null) {
			logger.warn("订单项状态有误，返回订单页");
			return "redirect:/order/0/10";
		}
		ProductOrder order=productOrderService.get(orderItem.getProductOrderItem_order().getProductOrder_id());
		if (order==null || order.getProductOrder_status() !=3) {
			logger.warn("订单项状态有误，返回订单页");
			return "redirect:/order/0/10";
		}
		if (reviewService.getTotalByOrderItemId(orderItem_id)>0) {
			logger.warn("订单项所属商品已被评价，返回订单页");
            return "redirect:/order/0/10";
		}
		 logger.info("获取订单项所属产品信息和产品评论信息");
		 Product product = productService.get(orderItem.getProductOrderItem_product().getProduct_id());
		 product.setProduct_review_count(reviewService.getTotalByProductId(product.getProduct_id()));
		 product.setSingleProductImageList(productImageService.getList(product.getProduct_id(), (byte) 0, new PageUtil(0,1)));
		 orderItem.setProductOrderItem_product(product);
		 map.put("orderItem", orderItem);
		 logger.info("转到前台天猫-评论添加页");
	     return "fore/addReview";
	}
	//添加一条评论
	@RequestMapping(value="review",method = RequestMethod.POST)
	public String addReview(HttpSession session, Map<String, Object> map,
            @RequestParam Integer orderItem_id,
            @RequestParam String review_content) throws UnsupportedEncodingException {
		logger.info("检查用户是否登录");
        Object userId = checkUser(session);
        User user;
        if (userId!=null) {
        	 logger.info("获取用户信息");
        	 user=userService.get(Integer.parseInt(userId.toString()));
        	 map.put("user", user);
		}else {
			return "redirect:/login";
		}
        logger.info("获取订单项信息");
        ProductOrderItem orderItem=productOrderItemService.get(orderItem_id);
		if (orderItem==null) {
			logger.warn("订单项不存在，返回订单页");
            return "redirect:/order/0/10";//返回到报错页面
		}
		if (!orderItem.getProductOrderItem_user().getUser_id().equals(userId)) {
			logger.warn("订单项与用户不匹配，返回订单页");
            return "redirect:/order/0/10";
		}
		if (orderItem.getProductOrderItem_order()==null) {
			logger.warn("订单项状态有误，返回订单页");
            return "redirect:/order/0/10";
		}
		ProductOrder order=productOrderService.get(orderItem.getProductOrderItem_order().getProductOrder_id());
		if (order==null||order.getProductOrder_status()!=3) {
			logger.warn("订单项状态有误，返回订单页");
            return "redirect:/order/0/10";
		}
		if (reviewService.getTotalByOrderItemId(orderItem_id)>0) {
			logger.warn("订单项所属商品已被评价，返回订单页");
            return "redirect:/order/0/10";
		}
		 logger.info("整合评论信息");
		 Review review=new Review().setReview_product(orderItem.getProductOrderItem_product())
				 .setReview_content(new String(review_content.getBytes("ISO-8859-1"), "UTF-8"))//中文编码避免乱码
				 .setReview_createDate(new Date())
				 .setReview_user(user)
				 .setReview_orderItem(orderItem);
		 logger.info("添加评论");
		Boolean yn=reviewService.add(review);
		if (!yn) {
			throw new RuntimeException();
		}
		return "redirect:/product/" + orderItem.getProductOrderItem_product().getProduct_id();
	}
	  //获取产品评论信息-ajax
	@ResponseBody
	@RequestMapping(value="review",method=RequestMethod.GET, produces = "application/json;charset=utf-8")
	public String getReviewInfo(@RequestParam("product_id") Integer product_id,
            @RequestParam("index") Integer index/* 页数 */,
            @RequestParam("count") Integer count/* 行数*/) {
		logger.info("获取产品评论信息");
		List<Review> reviewList=reviewService.getListByProductId(product_id,new PageUtil(index,10));
		if (reviewList!=null) {
			for (Review review : reviewList) {
				  review.setReview_user(userService.get(review.getReview_user().getUser_id()));
			}
		}
		Integer total=reviewService.getTotalByProductId(product_id);
		JSONObject object = new JSONObject();
		object.put("reviewList", reviewList);
		object.put("pageUtil", new PageUtil().setTotal(total).setIndex(index).setCount(count));
		return object.toJSONString();
	}
}
