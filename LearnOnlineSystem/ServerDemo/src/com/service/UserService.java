package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.beans.User;
import com.dao.Dao;

import utils.SpringUtils;

@Service("userService")
public class UserService {

	@Autowired
	private Dao dao;  //spring 注入dao

	//String 转 int
	private int StringToInt(String str) {
		try {
			return Integer.valueOf(str);
		}catch(Exception e){
			return 0;
		}
	}
	
	//登录验证
	public boolean adminlogincheck(String idStr,String pwd) 
	{
		int id = StringToInt(idStr);
		String sql = "select count(*) from tab_admin where admin_id = ? and admin_password = ?";	 //根据账号、密码，查询数据库	用户表
		if(1 == dao.selectCount(sql, id, pwd))
			return true;
		return false;
	}
	
	
	//登录验证
	public boolean logincheck(String idStr,String pwd) 
	{
		int id = StringToInt(idStr);		
		String sql = "select count(*) from tab_user where user_id = ? and user_password = ?";
		if(1 == dao.selectCount(sql, id, pwd))
			return true;
		return false;
	}
	

	//用户注册
	public long register(String name,String pwd) 
	{
		long id = 1000 + dao.getCount("tab_user");
		String sql = "insert into tab_user(user_id, user_name, user_password) values(?,?,?)";			
		if(1 == dao.add(sql, id, name, pwd))
			return id;
		return 0;
	}
	
	//删除用户
	public boolean deleteUser(int id) 
	{
		String sql = "delete from tab_user where user_id=?";
		if(1 == dao.delete(sql, id))
		    return true;
		else return false;		
	}
	
	//增加用户
	public long addUser(int id,String name,String pwd) 
	{
		if(1 < dao.selectCount("select count(*) from tab_user where user_id=?", id)) {
			return -1;
		}
		String sql = "insert into tab_user(user_id, user_name, user_password) values(?,?,?)";			
		if(1 == dao.add(sql, id, name, pwd))
			return id;
		return 0;
	}
	
	//获取用户信息
	public List<User> getUsers() 
	{	
		String sql = "select * from tab_user";
		return dao.selectList(sql, new BeanPropertyRowMapper<>(User.class));
	}

	public User getUser(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from tab_user where user_id=?";
		return dao.selectObject(sql, new BeanPropertyRowMapper<>(User.class),id);
	}
	

	public long modifyUser(int user_id, String user_name, String user_password) {
		// TODO Auto-generated method stub
		String sql = "update tab_user set user_id=?,user_name=?,user_password=? where user_id=?";
		return dao.modify(sql, user_id,user_name,user_password,user_id);
	}

}