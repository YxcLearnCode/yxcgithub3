package com.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beans.AnalysisData;
import com.beans.Answer;
import com.beans.ErrorTopic;
import com.beans.Exam;
import com.beans.Topic;
import com.service.ExamService;
import utils.JsonUtils;


@Controller
public class ExamController {
	
	@Autowired
	private ExamService examService;
	
	
	
	/**��ȡ����*/
	@RequestMapping("/getexam")  
	@ResponseBody
    public Exam getExam(String data)  
    {   	
		Exam exam = JsonUtils.JsonToObject(data, Exam.class);
		exam = examService.getExamByCourse(exam);
		System.out.println("getEXAM:"+ exam.toString());
		return exam;
    }
	
	
	/**����type��ȡ����*/
	@RequestMapping("/gettopicbytype")  
	@ResponseBody
    public List<Topic> getTopicByType(String data)  
    { 
//		Topic topic = new Topic();
//		topic.setCourse_id(11);
//		topic.setType(1);
		Topic topic = JsonUtils.JsonToObject(data, Topic.class);
		return examService.getTopicByType(topic);		
    }
	
	/**��� ��Ŀ*/
	@RequestMapping("/addtopic")  
	@ResponseBody
    public void addTopic(String data)
    {         		
		System.out.println(data);
		List<Topic> topic = JsonUtils.JsonToList(data, Topic.class);
	    examService.addTopic(topic);
    }  
	
	
	/**��ȡ�û���������*/
	@RequestMapping("/getexamresult")  
	@ResponseBody
    public List<Exam> getExamResult(int data)  
    {   
		List<Exam> examlist = examService.getExamByUserId(data);
		System.out.println("getEXAM:"+ examlist.toString());
		return examlist;
    } 
	
	
	/**ɾ����������*/
	@RequestMapping("/deltopic")  
	@ResponseBody
    public boolean deleteTopic(int data)  
    {   		
		return examService.deleteTopic(data);
    }  
	
	
	/**��ȡȫ������*/ 
	@RequestMapping("/geterrortopic")  
	@ResponseBody
	public List<AnalysisData> getErrorTopics(){
		return examService.getErrorTopics();		
	}
	
	
	/**�����û���ȡ����*/ 
	@RequestMapping("/geterrorbyuser")  
	@ResponseBody
	public List<Topic> getErrorTopicsByUser(int data){
		return examService.getErrorTopicsByUser(data);		
	}
	
	/**�ϴ���*/
	@RequestMapping("/addanswer")  
	@ResponseBody
	public boolean addAnswer(String data) {
		System.out.println(data);
		List<Answer> answers = JsonUtils.JsonToList(data, Answer.class);
		return examService.addAnswer(answers);		
	}

}