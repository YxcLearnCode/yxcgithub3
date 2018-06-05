package com.beans;


public class User {
		
	private int user_id;    //用户id	
	private String user_name;  //用户名字	
	private String user_password;	//用户表密码 

	public User(int id, String name) {
		this.user_id = id;
		this.user_name = name;
	}
	
	public User() {};
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	@Override
	public String toString(){
		return "ID:"+ user_id + ",Name:"+ user_name + ",PWD:"+ user_password;
	}


	
}
