package com.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("sseService")
public class SSEService {
	
	private static Map<String,List<String>> msgMap = new HashMap<>();
	
	/**添加消息序列*/
	public void addMessage(com.beans.Message message) {
		String key = String.valueOf(message.getUser_id());
		List<String> list;
		if(null == msgMap.get(key)) {
			list = new ArrayList<>();
		}else {
			list = msgMap.get(key);			
		}
		list.add(message.getMessage());
		msgMap.put(key, list);		
	}
	
	
	/**获取消息*/
	public List<String> getMessage(int data){
		String key = String.valueOf(data);
		List<String> list = new ArrayList<>();
		if(null != msgMap.get(key)) {
			list.addAll(msgMap.get(key));			
			msgMap.get(key).clear();
		}
		return list;
	}

}
