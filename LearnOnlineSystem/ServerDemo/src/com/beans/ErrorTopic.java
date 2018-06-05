package com.beans;

import java.util.List;

//错题本
public class ErrorTopic {
	 
    private String user_id;  //用户id
    private List<Topic> topics;    //错题集

    
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public List<Topic> getTopics() {
		return topics;
	}
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

}
