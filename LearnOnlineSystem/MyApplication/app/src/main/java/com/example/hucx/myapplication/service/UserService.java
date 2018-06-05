package com.example.hucx.myapplication.service;

import android.os.Handler;

import com.example.hucx.myapplication.Utils.HttpCon;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.User;


public class UserService {

    public static UserService instance;
    public int user_id;

    public static UserService getInstance() {
        if(null == instance){
            instance = new UserService();
        }
        return instance;
    }

    //封装的请求
    private void request(String request,String data,Handler handler){
        HttpCon.request(request, data, handler);
    }

    //登录请求
    public void loginCheck(User bean, Handler handler){
        String json = JsonUtils.ObjectToJson(bean);
        request("login",json,handler);
    }

    //注册请求
    public void registerCheck(User bean, Handler handler){
        String json = JsonUtils.ObjectToJson(bean);
        request("register",json,handler);
    }


}
