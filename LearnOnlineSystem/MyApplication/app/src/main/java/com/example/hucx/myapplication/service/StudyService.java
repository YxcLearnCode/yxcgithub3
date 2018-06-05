package com.example.hucx.myapplication.service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.Utils.HttpCon;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.AnalysisData;
import com.example.hucx.myapplication.beans.Plan;
import com.example.hucx.myapplication.beans.Study;

import java.util.ArrayList;
import java.util.List;


public class StudyService {

    public static StudyService instance;
    public List<Plan> plans = new ArrayList();    //计划列表
    public List<String> remindBeans = new ArrayList();   //提醒列表
    private Context context; //提示


    public static StudyService getInstance() {
        if(null == instance){
            instance = new StudyService();
        }
        return instance;
    }

    public void setContext(Context context){
        this.context = context;
    }

    //设置计划列表
    public void setPlanBean(List<Plan> plans){
        this.plans.clear();
        this.plans.addAll(plans);
    }

    //获取计划
    public List<String> getPlanBean(){
        List<String> list = new ArrayList();
        for(Plan bean: plans){
            String name = CourseService.getInstance().getCourseNameById(bean.getCourse_id());
            if(null != name) {
                list.add(bean.getTime() + "计划学习" + name);
            }
        }
        return list;
    }


    //设置提醒
    public void setRemindBean(List<String> list){
        remindBeans.addAll(list);
    }


    //获取提醒
    public List<String> getRemindBean(){
        return remindBeans;
    }

    //封装的请求
    private void request(String request,String data,Handler handler){
        HttpCon.request(request, data, handler);
    }

    //请求获取计划
    public void requestGetPlan(Handler handler){
        String json = String.valueOf(UserService.getInstance().user_id);
        request("getplan",json,handler);
    }

    //请求删除计划
    public void requestDelPlan(int planid, Handler handler){
        String json = String.valueOf(planid);;
        request("delplan ",json,handler);
    }

    //请求添加计划
    public void requestAddPlan(Plan plan, Handler handler){
        String json = JsonUtils.ObjectToJson(plan);
        request("addplan ",json,handler);
    }

    //请求科目进度
    public void requestCourseProgress(Study study, Handler handler){
        String json = JsonUtils.ObjectToJson(study);
        request("getprogress",json,handler);
    }

    //请求修改科目进度
    public void requestAddProgress(AnalysisData data, Handler handler){
        String json = JsonUtils.ObjectToJson(data);
        request("addprogress",json,handler);
    }


    //请求获取学习记录
    public void requestGetStydy(Study study, Handler handler){
        String json = JsonUtils.ObjectToJson(study);
        request("getuserstudy",json,handler);
    }


    //请求增加学习记录
    public void requestAddStydy(Study study, Handler handler){
        String json = JsonUtils.ObjectToJson(study);
        request("addstudy",json,handler);
    }

    //获取学习提醒
    public void requestMessage(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String json = String.valueOf(UserService.getInstance().user_id);
                request("getmessage",json,handler);
            }
        }, 4000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String message = b.getString("msg");
            String request = b.getString("request");
            requestMessage();

            if("请求失败".equals(message)){
                AlertUtils.Alert(context,"连接服务器失败！", null);
            }
            else if("getmessage".equals(request)){
                List<String> list = JsonUtils.JsonToList(message,String.class);
                setRemindBean(list);
                if(null != list && list.size()>0 && null !=context) {
                    Toast.makeText(context, list.get(0), Toast.LENGTH_LONG).show();
                }
            }
        }
    };

}
