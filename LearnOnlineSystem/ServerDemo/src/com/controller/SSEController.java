package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.service.SSEService;

import utils.JsonUtils;

//��������app����������Ϣ
@Controller
public class SSEController {
	
	@Autowired
	private SSEService sseService;
    
	/**�����Ϣ����*/
	@RequestMapping("/addmessage")  
	@ResponseBody
    public boolean addMessage(String data)  
    {   	
		System.out.println(data);
		com.beans.Message message = JsonUtils.JsonToObject(data, com.beans.Message.class);
		sseService.addMessage(message);
		return true;
    }  
	
	
	/**��ȡ��Ϣ*/
	@RequestMapping("/getmessage")  
	@ResponseBody
    public List<String> getMessage(int data)  
    {   
		return sseService.getMessage(data);
    } 
    
}