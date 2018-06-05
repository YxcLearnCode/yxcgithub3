package com.example.hucx.myapplication.service;

import android.os.Handler;

import com.example.hucx.myapplication.Utils.HttpCon;
import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.beans.CourseContent;

import java.util.ArrayList;
import java.util.List;


public class CourseService {

    public static CourseService instance;
    public List<Course> courseList = new ArrayList();
    public List<Course> studycourseList = new ArrayList();
    public List<CourseContent> courseContentsList = new ArrayList();
    public int courseId;  //当前科目id
    //public int index = -1; //当前kemu深度


    //获取实例
    public static CourseService getInstance(){
        if(null == instance){
            instance = new CourseService();
        }
        return instance;
    }

//    //获取当前课程id
//    public int getCourseId(){
//        return courseId;
//    }

//    //获取当前课程Shendu
//    public int getIndex(){
//        return index;
//    }


//    //设置当前课程id
//    public void setCourseId(int index, int flag){
//        //this.index = index;
//        this.courseId = getCourseBean(index).getCourse_id();
//    }



    //设置科目内容数据
    public void setCourseContentsList(List<CourseContent> beans){
        courseContentsList.clear();
        if(null != beans) {
            courseContentsList.addAll(beans);
        }
    }

    //设置科目列表数据
    public void setCourseList(List<Course> bean){
        courseList.clear();
        courseList.addAll(bean);
    }

    //设置学习科目列表数据
    public void setStudyCourseList(List<Course> bean){
        studycourseList.clear();
        studycourseList.addAll(bean);
    }

    //根据索引获取科目内容
    public CourseContent getCourseText(int index){
        return courseContentsList.get(index );
    }

    //获取学习科目列表
    public List<Course> getStudycourseList(){
        return studycourseList;
    }

    //根据索引获取科目
    public Course getStudyCourse(int courseId){
        for(Course course:studycourseList){
            if(course.getCourse_id() == courseId) {
                return course;
            }
        }
        return null;
    }


    //根据索引获取科目
    public Course getCourse(int courseId){
        for(Course course:courseList){
            if(course.getCourse_id() == courseId) {
                return course;
            }
        }
        return null;
    }


    // 获取科目内容列表
    public List<String> getContentList(){
        List<String> list = new ArrayList();
        for(CourseContent bean:courseContentsList){
            list.add(bean.getContent_name());
        }
        return list;
    }

    public String getCourseNameById(int course_id) {
        for(Course bean: courseList){
            if(course_id == bean.getCourse_id()){
                return bean.getCourse_name();
            }
        }
        return null;
    }

    //封装的请求
    private void request(String request,String data,Handler handler){
        HttpCon.request(request, data, handler);
    }

    //从服务器获取科目列表
    public void requestCourses(Handler handler){
        request("getcourse",null,handler);
    }

    //从服务器获取科目列表
    public void requestStudyCourses(Handler handler){
        String data = String.valueOf(UserService.getInstance().user_id);
        request("getstudycourse",data,handler);
    }
    //从服务器获取科目内容列表
    public void requestContents(int courseId,Handler handler){
        String data = String.valueOf(courseId);
        request("getcontents",data,handler);
    }

}
