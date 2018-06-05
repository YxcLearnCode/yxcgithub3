package com.beans;


public class Course {
		
	private String course_id;    //课程id
	private String course_name;  //课程名字
	private String course_teacher; //讲师
	private String course_introduction; //课程简介
	
	public Course(String id, String name) {
		this.course_id = id;
		this.course_name = name;
	}
	
	public Course() {};
	
	public String getCourse_id() {
		return course_id;
	}
	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}
	public String getCourse_name() {
		return course_name;
	}
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getCourse_teacher() {
		return course_teacher;
	}

	public void setCourse_teacher(String course_teacher) {
		this.course_teacher = course_teacher;
	}

	public String getCourse_introduction() {
		return course_introduction;
	}

	public void setCourse_introduction(String course_introduction) {
		this.course_introduction = course_introduction;
	}
}
