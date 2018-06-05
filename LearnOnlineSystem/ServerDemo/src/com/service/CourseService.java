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
	
	//spring 注入的数据库操作类dao,
	@Autowired
	private Dao dao;	
	
    //随机获取课程
	public List<Course> getCourses()
	{
		String sql = "select * from tab_course order by rand() limit 10";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(Course.class));
	}	

	
	//增加课程
	public boolean addCourse(Course course)
	{
		String sql = "insert into tab_course(course_name, course_teacher, course_introduction) values(?,?,?)";  			
		if(1 == dao.add(sql, course.getCourse_name(), course.getCourse_teacher(), course.getCourse_introduction()))
			return true;
		return false;
	}
	
	
	//修改课程
	public boolean modifyCourse(int courseid, String newname) 
	{
		String sql = "update tab_course set course_name=? where course_id=?";	
		if(1 == dao.modify(sql, newname, courseid))
			return true;
		return false;		
	}
	
	
	//删除课程
	public boolean deleteCourse(int id) 
	{
		String sql = "delete from tab_course where course_id=?";
		if(1 == dao.delete(sql, id))
			return true;
		return false;		
	}
	
	
	
    //获取课程内容
	public List<CourseContent> getCourseContens(int id)
	{
		String sql = "select * from tab_coursecontents where course_id=?";		
		return dao.selectList(sql, new BeanPropertyRowMapper<>(CourseContent.class), id);
	}
	
	
	//增加内容
	public boolean addCourseConten(int courseid,String contentname)
	{
		String sql = "insert into tab_coursecontents(course_id,content_name) values(?,?)";  			
		if(1 == dao.add(sql, courseid, contentname))
			return true;
		return false;
	}
	
	
	//修改内容
	public boolean modifyCourseContent(int id, String name, String text) 
	{
		String sql = "update tab_coursecontents set content_name=?,text=? where id=?";	
		if(1 == dao.modify(sql, name, text, id))
			return true;
		return false;		
	}
	
	
	//删除内容
	public boolean deleteCourseConten(int id) 
	{
		String sql = "delete from tab_coursecontents where id=?";
		if(1 == dao.delete(sql, id))
			return true;
		return false;		
	}


	//根据id获取内容
	public CourseContent getCourseContentById(int id) {
		// TODO Auto-generated method stub
		String sql = "select * from tab_coursecontents where id=?";		
		return dao.selectObject(sql, new BeanPropertyRowMapper<>(CourseContent.class), id);
	}
}