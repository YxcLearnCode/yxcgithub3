package com.controller;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.beans.Course;
import com.beans.CourseContent;
import com.service.CourseService;

import utils.JsonUtils;
 

@Controller
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	//��ӿ�Ŀ
	@RequestMapping("/addcourse") 
	@ResponseBody
    public boolean addCourse(String data)  
    {   
		Course course = JsonUtils.JsonToObject(data, Course.class);
        return courseService.addCourse(course);
    } 
	
	//ɾ����Ŀ
	@RequestMapping("/delcourse") 
	@ResponseBody
    public boolean delCourse(int data)  
    {
		return courseService.deleteCourse(data);
    } 
	
	//�����ȡ��Ŀ
	@RequestMapping("/getcourse") 
	@ResponseBody
    public List<Course> getCourses()  
    {   
        List<Course> msg = null;        
        if(null == (msg = courseService.getCourses()))
		{
        	msg = new ArrayList<Course>();
		} 
        System.out.println("getcourse:return " + msg);
        return msg;  
    }
	
	//��ȡ�����б�
	@RequestMapping("/getcontents")
	@ResponseBody
    public List<CourseContent> getContents(int data)  
    {   
        List<CourseContent> msg = null;        
        if(null == (msg = courseService.getCourseContens(data)))
		{
        	msg = new ArrayList<CourseContent>();
		} 
        System.out.println("getcoursecontents:return " + msg);
        return msg;  
    }
	
	
	//�������
	@RequestMapping("/addcontent") 
	@ResponseBody
    public boolean addContent(String data)  
    {   
		if(null != data) {
		  CourseContent content = JsonUtils.JsonToObject(data, CourseContent.class);
          return courseService.addCourseConten(content.getCourse_id(), content.getContent_name());
		}
		return false;
    } 
	
	
	//ɾ������
	@RequestMapping("/delcontent") 
	@ResponseBody
    public boolean delContent(int data)  
    {
		return courseService.deleteCourseConten(data);
    }
	
	
	//�޸�����
	@RequestMapping("/modifycontent") 
	@ResponseBody
    public boolean modifyContent(String data)  
    {
		System.err.println(data);
		if(null != data) {
	    	CourseContent content = JsonUtils.JsonToObject(data, CourseContent.class);
	    	return courseService.modifyCourseContent(content.getId(),content.getContent_name(),content.getText());
		}
		return false; 
    }
	
	
	//����id��ȡ����
	@RequestMapping("/getcontent")
	@ResponseBody
    public CourseContent getContentById(int data)  
    {   
		System.out.println("showContent:" + data);
	    return courseService.getCourseContentById(data);
    } 
}