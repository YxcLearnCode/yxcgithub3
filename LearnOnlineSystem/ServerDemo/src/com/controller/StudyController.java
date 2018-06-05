package com.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.beans.AnalysisData;
import com.beans.Course;
import com.beans.Plan;
import com.beans.Study;
import com.service.StudyService;
import com.service.UserService;

import utils.JsonUtils;
 

@Controller
public class StudyController {
	
	@Autowired
	private StudyService studyService;
	
	/**获取计划*/
	@RequestMapping("/getplan") 
	@ResponseBody
    public List<Plan> getPlan(int data)  
    {  
		System.out.println(data);
       return studyService.getPlans(data);
    }
	
	/**添加计划*/
	@RequestMapping("/addplan") 
	@ResponseBody
    public boolean addPlan(String data)  
    {  
		System.out.println(data);
		Plan plan = JsonUtils.JsonToObject(data, Plan.class);
        return studyService.addPlan(plan);
    } 
	
	
	/**删除计划*/
	@RequestMapping("/delplan") 
	@ResponseBody
    public boolean deletePlan(int data)   
    {  		
        return studyService.deletePlan(data);
    } 
	
	
	/** 获取课程学习状态  */	
	@RequestMapping("/getuserstudy") 
	@ResponseBody
    public List<Study> getUserStudy(int data)  
    {  
        return studyService.getUserStudy(data);
    } 
	
	
	/**修改课程状态*/
	@RequestMapping("/modifystudystate") 
	@ResponseBody
    public boolean modifyStudyState(String data)  
    {  
		Study study = JsonUtils.JsonToObject(data, Study.class);
        return studyService.modifyStudyState(study);
    } 
	
	/**添加学习课程*/
	@RequestMapping("/getstudycourse") 
	@ResponseBody
    public List<Course> getStudyCourse(int data)
    {  
		System.out.println("getstudycourse:" +data);		
        return studyService.getStudyCourse(data);
    } 
	
	/**添加学习课程*/
	@RequestMapping("/addstudy") 
	@ResponseBody
    public boolean addStudy(String data)
    {  
		System.out.println("addStudy:" +data);
		Study study = JsonUtils.JsonToObject(data, Study.class);
        return studyService.addStudy(study);
    } 
	
	
	/**获取科目学习进度*/
	@RequestMapping("/getprogress") 
	@ResponseBody
    public AnalysisData getProgress(String data)
    {  
		System.out.println("getProgress:" +data);
		Study study = JsonUtils.JsonToObject(data, Study.class);
        return studyService.getCourseProgress(study);
    }
	
	/**获取科目学习进度*/
	@RequestMapping("/addprogress") 
	@ResponseBody
    public boolean addProgress(String data)
    {  
		System.out.println("addProgress:" + data);
		AnalysisData adata = JsonUtils.JsonToObject(data, AnalysisData.class);
        return studyService.addCourseProgress(adata);
    }
}