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
	private Dao dao;  //spring ע��dao

	//String ת int
	private int StringToInt(String str) {
		try {
			return Integer.valueOf(str);
		}catch(Exception e){
			return 0;
		}
	}
	
	//��¼��֤
	public boolean adminlogincheck(String idStr,String pwd) 
	{
		int id = StringToInt(idStr);
		String sql = "select count(*) from tab_admin where admin_id = ? and admin_password = ?";	 //�����˺š����룬��ѯ���ݿ�	�û���
		if(1 == dao.selectCount(sql, id, pwd))
			return true;
		return false;
	}
	
	
	//��¼��֤
	public boolean logincheck(String idStr,String pwd) 
	{
		int id = StringToInt(idStr);		
		String sql = "select count(*) from tab_user where user_id = ? and user_password = ?";
		if(1 == dao.selectCount(sql, id, pwd))
			return true;
		return false;
	}
	

	//�û�ע��
	public long register(String name,String pwd) 
	{
		long id = 1000 + dao.getCount("tab_user");
		String sql = "insert into tab_user(user_id, user_name, user_password) values(?,?,?)";			
		if(1 == dao.add(sql, id, name, pwd))
			return id;
		return 0;
	}
	
	//ɾ���û�
	public boolean deleteUser(int id) 
	{
		String sql = "delete from tab_user where user_id=?";
		if(1 == dao.delete(sql, id))
		    return true;
		else return false;		
	}
	
	//�����û�
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
	
	//��ȡ�û���Ϣ
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