package com.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.beans.AnalysisData;
import com.beans.Answer;
import com.beans.ErrorTopic;
import com.beans.Exam;
import com.beans.Topic;
import com.dao.Dao;

import utils.JsonUtils;
import utils.SpringUtils;

@Service
public class ExamService {
	
	//spring 注入的数据库操作类dao,
	@Autowired
	private Dao dao;
	
	
	/**-------------- 试卷题目处理 --------------*/
	/**随机获取试题（10选择题，10填空题，2简答题）*/
	public Exam getExamByCourse(Exam exam)
	{			
		List<Topic> topics = new ArrayList<>();
		String[] sql = {
				"select * from tab_topic where course_id=? and type=0 order by rand() limit 0,10",
				"select * from tab_topic where course_id=? and type=1 order by rand() limit 0,10",
				"select * from tab_topic where course_id=? and type=2 order by rand() limit 0,2"
				};
		for(String str:sql) {
	    	List<Topic> list = dao.selectList(str, new BeanPropertyRowMapper<>(Topic.class), exam.getCourse_id());
	    	if(null != list) {
	    		topics.addAll(list);	
	    	}
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式  
		String time = df.format(new Date());
	    System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
	    
	    exam.setExam_id(time);
	    exam.setTopics(topics);
	    
	    addExam(exam);
	    
		return exam;
	}
	
	
	/**根据科目和类别获取题目*/
	public List<Topic> getTopicByType(Topic topic){
		String sql = "select * from tab_topic where type=? and course_id=?";
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Topic.class), topic.getType(), topic.getCourse_id());
	}
	
	/**录入用户考试数据*/
	public boolean addExam(Exam exam)
	{
		String sql = "delete from tab_exam where course_id=? and user_id=? and answer is null";
		dao.delete(sql, exam.getCourse_id(),exam.getUser_id());
		
		sql = "insert into tab_exam(exam_id,course_id, user_id, topic_id,iserror) values(?,?,?,?,?)";
		for(Topic topic:exam.getTopics()) {
			dao.add(sql, exam.getExam_id(), exam.getCourse_id(), exam.getUser_id(), topic.getId(), 1);  
		}
		return false;
	}
	
	/**录入考试答案*/
	public boolean addAnswer(List<Answer> answers) {
		String[] sql = {
				"select answer from tab_topic where id=? limit 1",
				"update tab_exam set answer=?,iserror=? where exam_id=? and topic_id=?"};
		for(Answer answer:answers) {
			Topic topic =dao.selectObject(sql[0], new BeanPropertyRowMapper<>(Topic.class), answer.getTopic_id());
			if(null != topic) {
//				int flag = 1;  //答案状态，0为正确，1为错误
//				if(2 == topic.getType()) {     //简答题
//					if(answer.getAnswer().contains(topic.getOption_a()) && answer.getAnswer().contains(topic.getOption_b()) && answer.getAnswer().contains(topic.getOption_c())) {
//						flag = 0;
//					}
//				}else if(1 == topic.getType()) {  //选择题
//					if(topic.getAnswer().equals(answer.getAnswer())) {
//						flag = 0;
//					}
//				}else if(0 == topic.getType()) {   //填空题
//					if(topic.getOption_a().equals(answer.getAnswer())) {
//						flag = 0;
//					}
//				}
	        	dao.modify(sql[1], answer.getAnswer(), answer.getIserror(), answer.getExam_id(), answer.getTopic_id());
			}
		}
		return true;		
	}
	
	/**根据用户id获取考试内容*/
	public List<Exam> getExamByUserId(int userid)
	{
		String sql = "select exam_id,course_id,user_id from tab_exam where user_id=? group by exam_id";
		List<Exam> examlist = dao.selectList(sql, new BeanPropertyRowMapper<>(Exam.class), userid);
		
		if(null != examlist && examlist.size()>0) {
			//搜索试题
			sql = "select b.* from tab_exam a,tab_topic b where a.topic_id=b.id and a.user_id=?";
			List<Topic> topics = dao.selectList(sql, new BeanPropertyRowMapper<>(Topic.class), userid);
			//搜索试题答案
			sql = "select exam_id,topic_id,answer,iserror from tab_exam where user_id=?";
			List<Answer> answers = dao.selectList(sql, new BeanPropertyRowMapper<>(Answer.class), userid);
			
			for(Exam exam:examlist) {
				List<Topic> topiclist = new ArrayList<>(); 				
		    	for(Topic topic:topics) {
			    	if(topic.getCourse_id() == exam.getCourse_id()) {
			    		topiclist.add(topic);
			    	}
		    	}
		    	
		    	List<Answer> answerlist = new ArrayList<>(); 				
		    	for(Answer answer:answers) {
			    	if(exam.getExam_id().equals(answer.getExam_id())) {
			    		answerlist.add(answer);
			    	}
		    	}
		    	exam.setTopics(topiclist);
		    	exam.setAnswers(answerlist);
			}
		}
		return examlist;
	}
	
	
	/**录入题目*/
	public boolean addTopic(List<Topic> topics)
	{
		String sql = "insert into tab_topic(type, course_id, question,option_a,option_b,option_c,option_d,answer) values(?,?,?,?,?,?,?,?)";
		for(Topic topic:topics) {
	     	dao.add(sql, topic.getType(), topic.getCourse_id(), topic.getQuestion(), topic.getOption_a(), topic.getOption_b(),topic.getOption_c(),topic.getOption_d(),topic.getAnswer());	       	
		}
		return false;
	}	
		
	
	/**删除试题*/
	public boolean deleteTopic(int topicid) 
	{
		String sql = "delete from tab_topic where id=?";
		if(1 == dao.delete(sql, topicid))
			return true;
		return false;		
	}
	
	
	
	/**-------------- 科目错题处理 --------------*/

	/**根据用户id获取错题*/
	public List<Topic> getErrorTopicsByUser(int userid)
	{
		String sql = "select a.* from tab_topic a,tab_exam b where a.id=b.topic_id and b.user_id=? and b.answer is not null and b.iserror=?";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Topic.class), userid,1);		
	}
	
	/**获取全部错题*/
	public List<AnalysisData> getErrorTopics()
	{
		String sql = "select course_id ,count(course_id) as count from tab_exam where iserror=? group by course_id";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(AnalysisData.class),1);		
	}

}