package com.example.hucx.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.beans.Exam;
import com.example.hucx.myapplication.beans.Topic;
import com.example.hucx.myapplication.service.CourseService;
import com.example.hucx.myapplication.service.ExamService;
import com.example.hucx.myapplication.service.StudyService;
import com.example.hucx.myapplication.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;


public class InfoManageActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    //定义控件
    //主窗口导航按钮
    private LinearLayout xuexiBtn;       //学习  按钮
    private LinearLayout zaixianceshiBtn; //在线测试  按钮
    private LinearLayout wodeBtn;        //我的  按钮

    private LinearLayout xuexiContent;    //学习内容  界面
    private ListView kemulist;  //科目列表
    private List<Course> kemudatas = new ArrayList<>(); //科目数据
    private ListViewAdapter kemuAdapter; //科目列表适配器

    //在线测试界面
    private LinearLayout zaixianceshiContent; //在线测试 内容界面
    private ListView study;      //试题列表
    private ArrayAdapter<String> studyCourseAdapter;  //试题列表适配器
    final private List<String>studydatas = new ArrayList<>(); //试题列表的数据


    //我的  界面按钮
    private LinearLayout wodeContent;   //我的  界面
    private TextView userId;       //用户id
    private LinearLayout btntongzhi;     //通知  界面

    private LinearLayout btnxuexijihua;   //学习计划  界面
    private LinearLayout btncuotiben;   //错题本  界面


    private CourseService courseService;  //科目业务处理
    private ExamService examService;  //科目业务处理
    private List<Course> studycourses; //学习的课程

    private static boolean isExit = false;  //返回键状态

    final android.os.Handler handler = new android.os.Handler() {

            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle b = msg.getData();
                String message = b.getString("msg");
                String request = b.getString("request");

                if("请求失败".equals(message)){
                    AlertUtils.Alert(InfoManageActivity.this,"无网络连接！",null);
                }

                if("getcourse".equals(request))
                {
                    List<Course> list = JsonUtils.JsonToList(message, Course.class);
                    courseService.setCourseList(list);
                    loadCourses(list);
                }else if("getstudycourse".equals(request))
                {
                    List<Course> list = JsonUtils.JsonToList(message, Course.class);
                    courseService.setStudyCourseList(list);
                    studycourses = list;
                    loadStudyCourse(list);
                }else if("geterrorbyuser".equals(request))
                {
                    //Snackbar.make(wodeBtn,message,Snackbar.LENGTH_LONG).show();
                    List<Topic> topics = JsonUtils.JsonToList(message,Topic.class);
                    examService.setErrorTopics(topics);
                }
                //Snackbar.make(xuexiContent,message,Snackbar.LENGTH_LONG).show();
            }
        };

    //初始化控件
    private void initWidget(){
        //初始化内容界面
        xuexiContent = (LinearLayout) findViewById(R.id.content_xuexi_info);
        wodeContent = (LinearLayout) findViewById(R.id.content_wode_info);
        zaixianceshiContent = (LinearLayout) findViewById(R.id.content_zaixianceshi_info);


        //初始化按键
        xuexiBtn = (LinearLayout) findViewById(R.id.button_xuexi_info);
        wodeBtn = (LinearLayout) findViewById(R.id.button_wode_info);
        zaixianceshiBtn = (LinearLayout) findViewById(R.id.button_study_info);

        kemulist = findViewById(R.id.list_kemu_info);
        kemuAdapter = new ListViewAdapter(kemudatas, InfoManageActivity.this);
        kemulist.setAdapter(kemuAdapter);

        btntongzhi = (LinearLayout)findViewById(R.id.btn_tongzhi_wode);
        btnxuexijihua = (LinearLayout)findViewById(R.id.btn_xuexijihua_wode);
        btncuotiben = (LinearLayout) findViewById(R.id.btn_cuotiben_wode);
        userId = (TextView) findViewById(R.id.textView_zhanghao_wode);

        //设置点击响应
        xuexiBtn.setOnClickListener(this);
        wodeBtn.setOnClickListener(this);
        zaixianceshiBtn.setOnClickListener(this);

        btntongzhi.setOnClickListener(this);
        btnxuexijihua.setOnClickListener(this);
        btncuotiben.setOnClickListener(this);

        //测试 试题列表
        study =(ListView) findViewById(R.id.list_studycourse_study);
        studyCourseAdapter = new ArrayAdapter<String>( InfoManageActivity.this, android.R.layout.simple_list_item_1, studydatas);
        study.setAdapter(studyCourseAdapter);
        study.setOnItemClickListener(this);
    }

    //加载试题数据
    public void loadStudyCourse(List<Course> list){
        studydatas.clear();
        for(Course course:list){
            studydatas.add(course.getCourse_name());
        }
        studyCourseAdapter.notifyDataSetChanged();  //更新数据
    }

    public void loadUserId(String id){
        if(null != id) {
            userId.setText(id);
        }
    }

    public void loadCourses(List<Course> list){
        kemudatas.clear();
        kemudatas.addAll(list);
        kemuAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_info_manage);

        courseService = CourseService.getInstance();
        examService = ExamService.getInstance();

        //开启提醒
        StudyService studyService = StudyService.getInstance();
        studyService.setContext(InfoManageActivity.this);
        studyService.requestMessage();
        initWidget();

        String user_id = getIntent().getStringExtra("user_id");
        loadUserId(user_id);   //设置ID
    }

    @Override
    protected void onResume() {
        courseService.requestCourses(handler);  //获取科目列表
        courseService.requestStudyCourses(handler);  //获取科目列表
        examService.requestErrorTopic(handler);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (isExit)
            finish();
        else {
            isExit = true;
            Toast.makeText(InfoManageActivity.this, "再按一次后退键退出账户", Toast.LENGTH_SHORT).show();
            new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isExit = false;
                }
            }.sendEmptyMessageDelayed(0, 2000);
        }
    }


    @Override
    public void onClick(View v) {   //点击响应
        Intent intent;   //通知响应定义
        switch (v.getId()) {
            case R.id.button_xuexi_info:  //学习
                xuexiContent.setVisibility(View.VISIBLE);  //设置显示
                zaixianceshiContent.setVisibility(View.GONE);   //设置隐藏
                wodeContent.setVisibility(View.GONE);
                break;

            case R.id.button_study_info: //在线测试
                xuexiContent.setVisibility(View.GONE);
                zaixianceshiContent.setVisibility(View.VISIBLE);
                wodeContent.setVisibility(View.GONE);
                break;

            case R.id.button_wode_info:  //我的
                xuexiContent.setVisibility(View.GONE);
                zaixianceshiContent.setVisibility(View.GONE);
                wodeContent.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_tongzhi_wode:  //通知
               intent = new Intent(InfoManageActivity.this,TongzhiActivity.class);
               startActivity(intent);
               break;

            case R.id.btn_xuexijihua_wode:  //学习计划
               intent= new Intent(InfoManageActivity.this,xuexiJihuaActivity.class);
               startActivity(intent);
               break;

            case R.id.btn_cuotiben_wode:  //错题本
                intent= new Intent(InfoManageActivity.this,CuotibenActivity.class);
                startActivity(intent);
                break;

        }
    }

    //列表点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;   //通知响应定义
        final int index = position;
        if(parent == study){
                intent = new Intent(InfoManageActivity.this,KemuActivity.class); //跳转到kemu界面
                intent.putExtra("courseid",studycourses.get(index).getCourse_id());
                intent.putExtra("flag",1);
                startActivity(intent);
        }
    }
}
