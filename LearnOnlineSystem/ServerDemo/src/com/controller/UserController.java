package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.beans.Course;
import com.beans.User;
import com.service.CourseService;
import com.service.UserService;

import utils.JsonUtils;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@RequestMapping("/adminlogin")
	public ModelAndView AdminLogin(String id, String password) {
		System.err.println("����Ա��¼");
		String page = "";
		String msg = null;
		ModelAndView mov = new ModelAndView();
		page = "AdminLogin";

		if ("" == id || null == id)
		{
			msg = "��¼������Ϊ��";
		}
		else if ("" == password || null == password)
		{
			msg = "���벻��Ϊ��ֵ";
		}
		else {
			if (userService.adminlogincheck(id, password)) 
			{
				page = "CoursePage";
				msg = id + "��ӭ���ĵ�¼��";				
			}
			else 
			{
				msg = "�˺����벻ƥ�䣬��¼ʧ�ܣ�";
			}
		}
		mov.addObject("msg", msg);
		mov.setViewName(page);
		return mov;
	}

	@ResponseBody
	@RequestMapping("/login")
	public boolean UserLogin(String data) {
		User user = JsonUtils.JsonToObject(data, User.class);
		return userService.logincheck(String.valueOf(user.getUser_id()), user.getUser_password());
	}

	@ResponseBody
	@RequestMapping("/register")
	public long UserRegister(String data) {
		User user = JsonUtils.JsonToObject(data, User.class);
		return userService.register(String.valueOf(user.getUser_id()), user.getUser_password());		
	}
	
	@ResponseBody
	@RequestMapping("/adduser")
	public long addUser(String data) {
		User user = JsonUtils.JsonToObject(data, User.class);
		return userService.addUser(user.getUser_id(), user.getUser_name(), user.getUser_password());		
	}
	
	
	@ResponseBody
	@RequestMapping("/updateuser")
	public long modifyUser(String data) {
		User user = JsonUtils.JsonToObject(data, User.class);
		return userService.modifyUser(user.getUser_id(), user.getUser_name(), user.getUser_password());		
	}
	
	@ResponseBody
	@RequestMapping("/deluser")
	public boolean deleteUser(int data) {
		return userService.deleteUser(data);		
	}
	
	@ResponseBody
	@RequestMapping("/getusers")
	public List<User> getUsers() {
		return userService.getUsers();		
	}
	
	@ResponseBody
	@RequestMapping("/getuser")
	public User getUserById(int data) {
		return userService.getUser(data);		
	}
}