package com.example.hucx.myapplication.service;

import android.os.Handler;

import com.example.hucx.myapplication.Utils.HttpCon;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.Answer;
import com.example.hucx.myapplication.beans.Exam;
import com.example.hucx.myapplication.beans.Topic;

import java.util.ArrayList;
import java.util.List;


public class ExamService {

    public static ExamService instance;
    public Exam exam = new Exam();
    public List<Topic> errorTopics = new ArrayList<>();

    public static ExamService getInstance() {
        if(null == instance){
            instance = new ExamService();
        }
        return instance;
    }

    //设置试题数据
    public List<Topic> getTopics(){
       return exam.getTopics();
    }

    //获取试题ID
    public String getExamId(){
        return exam.getExam_id();
    }

    //根据课程获取错题数据
    public List<Topic> getErrorTopicsByCourse(int courseid){
        List<Topic> list = new ArrayList<>();
        for(Topic topic:errorTopics){
            if(topic.getCourse_id() == courseid){
                list.add(topic);
            }
        }
        return list;
    }

    //设置错题数据
    public void setErrorTopics(List<Topic> topics){
        errorTopics.clear();
        errorTopics.addAll(topics);
    }

    //设置试题数据
    public void setExam(Exam exam){
        this.exam = exam;
    }

    //封装的请求
    private void request(String request,String data,Handler handler){
        HttpCon.request(request, data, handler);
    }

    //从服务器获取试题列表
    public void requestExams(int course_id,Handler handler){
        Exam exam = new Exam();
        exam.setCourse_id(course_id);
        exam.setUser_id(UserService.getInstance().user_id);
        String json = JsonUtils.ObjectToJson(exam);

        request("getexam", json, handler);
    }

    //请求获取错题
    public void requestErrorTopic(Handler handler){
        String json = String.valueOf(UserService.getInstance().user_id);
        request("geterrorbyuser", json, handler);
    }

    //请求提交考试答案
    public void requestSubmitAnswer(List<Answer> answers, Handler handler){
        String json = JsonUtils.ObjectToJson(answers);
        request("addanswer", json, handler);
    }
}
