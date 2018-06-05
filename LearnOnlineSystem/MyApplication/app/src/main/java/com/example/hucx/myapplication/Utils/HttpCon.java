package com.example.hucx.myapplication.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class HttpCon {

    private static String serverpath = "http://172.16.169.16:8080/XueXiServerDemo/";

    private static void sendPost(final String request, final String params, Handler msghandler){
        final Handler handler = msghandler;
        final String urlStr = serverpath + request;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                    httpconn.setRequestProperty("accept", "*/*");
                    httpconn.setRequestProperty("Accept-Charset", "UTF-8");
                    httpconn.setRequestProperty("contentType", "UTF-8");
                    httpconn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
                    httpconn.setRequestMethod("POST");
                    httpconn.setDoInput(true);
                    httpconn.setDoOutput(true);

                    // 往服务器里面发送数据
                    // 获取URLConnection对象对应的输出流传
                    PrintWriter printWriter = new PrintWriter(httpconn.getOutputStream());
                    // 发送请求参数
                    printWriter.write(params);//post的参数 xx=xx&yy=yy
                    // flush输出流的缓冲
                    printWriter.flush();

                    httpconn.setConnectTimeout(5000);
                    httpconn.connect();
                    int stat = httpconn.getResponseCode();
                    String ss = httpconn.getRequestMethod();
                    Log.i("Tag", "CODE:" + stat);
                    String msg = "";
                    if (stat == 200) {
                        br = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
                        msg = br.readLine();
                        Log.i("Tag", "msg" + msg);
                        Bundle b = new Bundle();
                        b.putString("request", request);
                        b.putString("msg", msg);
                        Message m = new Message();
                        m.setData(b);
                        handler.sendMessage(m);
                    } else {
                        msg = "请求失败";
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public static void request(String request,String data, final Handler msgHandler) {
        String params = fomatRequestData(data);
        sendPost(request, params, msgHandler);
    }

    //格式化参数
    private static String fomatRequestData(String data){
        String urlStr = "data=" + data;
//        if(null != map && 0<map.size()) {
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                String key = entry.getKey().toString();
//                String value = entry.getValue().toString();
//                urlStr += key + "=" + value + "&";
//            }
//            int indx = urlStr.lastIndexOf("&");
//            if (indx != -1) {
//                urlStr = urlStr.substring(0, indx);
//            }
//        }
        return urlStr;
    }
}
