package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import com.beans.Course;
import com.beans.CourseContent;
import com.dao.Dao;

@Service
public class CourseService {
	
	//spring ע������ݿ������dao,
	@Autowired
	private Dao dao;	
	
    //�����ȡ�γ�
	public List<Course> getCourses()
	{
		String sql = "select * from tab_course order by rand() limit 10";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Course.class));
	}	

	
	//���ӿγ�
	public boolean addCourse(Course course)
	{
		String sql = "insert into tab_course(course_name, course_teacher, course_introduction) values(?,?,?)";  			
		if(1 == dao.add(sql, course.getCourse_name(), course.getCourse_teacher(), course.getCourse_introduction()))
			return true;
		return false;
	}
	
	
	//�޸Ŀγ�
	public boolean modifyCourse(int courseid, String newname) 
	{
		String sql = "update tab_course set course_name=? where course_id=?";	
		if(1 == dao.modify(sql, newname, courseid))
			return true;
		return false;		
	}
	
	
	//ɾ���γ�
	public boolean deleteCourse(int id) 
	{
		String sql = "delete from tab_course where course_id=?";
		if(1 == dao.delete(sql, id))
			return true;
		return false;		
	}
	
	
	
    //��ȡ�γ�����
	public List<CourseContent> getCourseContens(int id)
	{
		String sql = "select * from tab_coursecontents where course_id=?";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(CourseContent.class), id);
	}
	
	
	//��������
	public boolean addCourseConten(int courseid,String contentname)
	{
		String sql = "insert into tab_coursecontents(course_id,content_name) values(?,?)";  			
		if(1 == dao.add(sql, courseid, contentname))
			return true;
		return false;
	}
	
	
	//�޸�����
	public boolean modifyCourseContent(int id, String name, String text) 
	{
		String sql = "update tab_coursecontents set content_name=?,text=? where id=?";	
		if(1 == dao.modify(sql, name, text, id))
			return true;
		return false;		
	}
	
	
	//ɾ������
	public boolean deleteCourseConten(int id) 
	{
		String sql = "delete from tab_coursecontents where id=?";
		if(1 == dao.delete(sql, id))
			return true;
		return false;		
	}


	//����id��ȡ����
	public CourseContent getCourseContentById(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from tab_coursecontents where id=?";		
		return dao.selectObject(sql, new BeanPropertyRowMapper<>(CourseContent.class), id);
	}
}