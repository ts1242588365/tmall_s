
package com.xq.tmall.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Admin;
import com.xq.tmall.service.AdminService;


/**
 * 后台管理-账户页
 */
@Controller
public class AccountController extends BaseController{
	//管理员的接口层
	@Resource(name="adminService")
	private AdminService adminService;
	
   //转到后台管理-账户页-ajax
	@RequestMapping(value="admin/account",method= RequestMethod.GET)
	public String goToPage(HttpSession session, Map<String, Object> map){
		logger.info("检查管理员权限");
		Object adminId = checkAdmin(session);
		  if(adminId == null){
	            return "admin/include/loginMessage";
	        }

	        logger.info("获取目前登录的管理员信息，管理员ID：{}",adminId);
	        Admin admin = adminService.get(null,Integer.parseInt(adminId.toString()));
	        map.put("admin",admin);

	        logger.info("转到后台管理-账户页-ajax方式");
	        return "admin/accountManagePage";
	}
	
	//退出当前账号
	@RequestMapping(value="admin/account/logout",method= RequestMethod.GET)
	public String logout(HttpSession session) {
		Object o = session.getAttribute("adminId");
		if(o==null) {
			logger.info("无管理员权限");
			
		}else {
			//移除session中的adminId的属性
			session.removeAttribute("adminId");
			//移除所有的与此用户关联的session对象
			session.invalidate();
			logger.info("登录信息已清除，返回管理员登陆页");
		}
		
		return "redirect:/admin/login";
		
	}	
	//管理员头像上传
	@ResponseBody
	@RequestMapping(value = "admin/uploadAdminHeadImage", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String uploadAdminHeadImage(@RequestParam MultipartFile file, HttpSession session) {
		String originalFilename = file.getOriginalFilename();
		logger.info("获取图片原始文件名：{}"+originalFilename);
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileName=UUID.randomUUID()+extension;	
		String filePath=session.getServletContext().getRealPath("/")+"res/images/item/adminProfilePicture/"+fileName;
		logger.info("文件上传路径：{}", filePath);
		
		JSONObject jsonObject=new JSONObject();
		 try {
	            logger.info("文件上传中...");
	            file.transferTo(new File(filePath));
	            logger.info("文件上传成功！");
	            jsonObject.put("success", true);
	            jsonObject.put("fileName", fileName);
	        } catch (IOException e) {
	            logger.warn("文件上传失败！");
	            e.printStackTrace();
	            jsonObject.put("success", false);
	        }
	        return jsonObject.toJSONString();
		
	}
	
	//更新管理员信息
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@ResponseBody
	@RequestMapping(value = "admin/account/{admin_id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
	public String updateAdmin(HttpSession session,@RequestParam String admin_nickname/*管理员昵称*/,
            @RequestParam(required = false) String admin_password/*管理员当前密码*/,
            @RequestParam(required = false) String admin_newPassword/*管理员新密码*/,
            @RequestParam(required = false) String admin_profile_picture_src/*管理员头像路径*/,
            @PathVariable("admin_id") String admin_id/*管理员编号*/) {
		 logger.info("检查管理员权限");
		 Object adminId = checkAdmin(session);
	        if (adminId == null) {
	            return "admin/include/loginMessage";
	        }
	        
	     JSONObject jsonObject=new JSONObject();
	     Admin putAdmin=new Admin();
	     putAdmin.setAdmin_id(Integer.valueOf(admin_id));
	     putAdmin.setAdmin_nickname(admin_nickname);
	     if (admin_password != null && !admin_password.equals("") && admin_newPassword != null && !admin_newPassword.equals("")) {
	            logger.info("获取需要修改的管理员信息");
	            Admin admin = adminService.get(null, Integer.valueOf(adminId.toString()));
	            if (adminService.login(admin.getAdmin_name(), admin_password) != null) {
	                logger.info("原密码正确");
	                putAdmin.setAdmin_password(admin_newPassword);
	            } else {
	                logger.info("原密码错误，返回错误信息");
	                jsonObject.put("success", false);
	                jsonObject.put("message", "原密码输入有误！");
	                return jsonObject.toJSONString();
	            }
	        }
	        if (admin_profile_picture_src != null && !admin_profile_picture_src.equals("")) {
	            logger.info("管理员头像路径为{}", admin_profile_picture_src);
	            putAdmin.setAdmin_profile_picture_src(admin_profile_picture_src.substring(admin_profile_picture_src.lastIndexOf("/") + 1));
	        }

	        logger.info("更新管理员信息，管理员ID值为：{}", admin_id);
	        Boolean yn = adminService.update(putAdmin);
	        if (yn) {
	            logger.info("更新成功！");
	            jsonObject.put("success", true);
	            session.removeAttribute("adminId");
	            session.invalidate();
	            logger.info("登录信息已清除");
	        } else {
	            jsonObject.put("success", false);
	            logger.warn("更新失败！事务回滚");
	            throw new RuntimeException();
	        }

	        return jsonObject.toJSONString();
	    }
	

}
