package com.example.hucx.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.AnalysisData;
import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.beans.CourseContent;
import com.example.hucx.myapplication.beans.Exam;
import com.example.hucx.myapplication.beans.Study;
import com.example.hucx.myapplication.beans.Topic;
import com.example.hucx.myapplication.service.CourseService;
import com.example.hucx.myapplication.service.ExamService;
import com.example.hucx.myapplication.service.StudyService;
import com.example.hucx.myapplication.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KemuActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private TextView kemutitle;    //标题
    private ImageView imageview; //科目图片
    private TextView jindu;    //进度
    private TextView jianjieBtn;       //科目简介  按钮
    private TextView muluiBtn;       //科目目录  按钮
    private LinearLayout muluContent;   //科目目录  界面
    private ScrollView jianjieContent;   //科目简介  界面

    private ListView mululist;      //目录列表
    private ArrayAdapter<String> muluAdapter;  //目录列表适配器
    final private List<String> muludatas = new ArrayList<>(); //目录列表的数据

    private TextView teacherName; //教师名字
    private TextView courseIntrodction; //课程简介

    private CourseService courseService;
    private Course course; //当前课程
    private int count = 0;  //当前已学习章节数
    private int[] image = {
            R.mipmap.image0,
            R.mipmap.image1,
            R.mipmap.image2,
            R.mipmap.image3,
            R.mipmap.image4,
            R.mipmap.image5,
            R.mipmap.image6,
            R.mipmap.image7,
            R.mipmap.image8,
            R.mipmap.image9,
            R.mipmap.image10,
            R.mipmap.image11,
            R.mipmap.image12,
            R.mipmap.image13,
            R.mipmap.image14,
            R.mipmap.image15,
            R.mipmap.image16,
            R.mipmap.image17,
            R.mipmap.image18,
            R.mipmap.image19,
            R.mipmap.image20,
            R.mipmap.image21
    };


    final android.os.Handler handler = new android.os.Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String message = b.getString("msg");
            String request = b.getString("request");

            if("请求失败".equals(message)){
                AlertUtils.Alert(KemuActivity.this,"无网络连接！",null);
            }
            if("getcontents".equals(request))
            {
                List<CourseContent> list = JsonUtils.JsonToList(message, CourseContent.class);
                courseService.setCourseContentsList(list);
                loadMulu(courseService.getContentList());
            }else if("getexam".equals(request))
            {
                JsonNode node = JsonUtils.getNode(message);
                Exam exam = new Exam();
                exam.setUser_id(UserService.getInstance().user_id);
                exam.setCourse_id(node.get("course_id").intValue());
                exam.setExam_id(node.get("exam_id").textValue());

                String topicsJson = node.get("topics").toString();
                List<Topic> topics = JsonUtils.JsonToList(topicsJson, Topic.class);
                exam.setTopics(topics);
                ExamService.getInstance().setExam(exam);
            }else if("getprogress".equals(request))
            {
                //Snackbar.make(jianjieBtn,message,Snackbar.LENGTH_LONG).show();
                AnalysisData data = JsonUtils.JsonToObject(message, AnalysisData.class);
                int total = CourseService.getInstance().getContentList().size();
                int percent = 0;
                if (0 != total) {
                    count = data.getCount();
                    percent = count * 100 /total;
                }
                jindu.setText(percent + "%");
                if(percent == 100){
                    AlertUtils.Alert(KemuActivity.this, "考试提醒", "您已学完该课程，是否要考试？", "开始考试", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(KemuActivity.this,ShitiJiemianActivity.class);  //跳转到试题内容界面
                            startActivity(intent);
                        }
                    });
                }

            }
            //Snackbar.make(xuexiContent,message,Snackbar.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kemu);

        findViewById(R.id.imageView_fanhui_kemu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        jianjieContent =  findViewById(R.id.content_kemujianjie_xuexi);
        muluContent = (LinearLayout) findViewById(R.id.content_kemumulu_xuexi);

        jindu  = findViewById(R.id. textView_xuexijindu_xuexi);
        teacherName = findViewById(R.id.textView_jiaoshimingzi_kemujianjie);
        courseIntrodction = findViewById(R.id.textView_neirong_kemujianjie);
        imageview = findViewById(R.id.imageView_courseimage_kemu);

        jianjieBtn = (TextView) findViewById(R.id.button_jianjie_xuexi);
        muluiBtn = (TextView) findViewById(R.id.button_mulu_xuexi);

        jianjieBtn.setOnClickListener(this);
        muluiBtn.setOnClickListener(this);

        //学习  科目目录列表
        mululist=(ListView) findViewById(R.id.list_mulu_kemumulu);
        muluAdapter = new ArrayAdapter<String>( KemuActivity.this, android.R.layout.simple_list_item_1, muludatas);
        mululist.setAdapter(muluAdapter);
        mululist.setOnItemClickListener(this);

        //科目标题
        kemutitle = findViewById(R.id.textView_kemuname_kemu);
        kemutitle.setOnClickListener(this);

        int courseid = getIntent().getIntExtra("courseid",0);
        int flag = getIntent().getIntExtra("flag",0);

        int index = new Random().nextInt(21);
        imageview.setImageResource(image[index]);

        courseService = CourseService.getInstance();
        courseService.requestContents(courseid, handler);//获取目录

        if(1 == flag) {
            course = courseService.getStudyCourse(courseid);
        }else{
            course = courseService.getCourse(courseid);
        }
        kemutitle.setText(course.getCourse_name());
        loadIntroduction(course.getCourse_teacher(), course.getCourse_introduction());
    }

    @Override
    protected void onResume() {
        Study study = new Study();
        study.setCourse_id(course.getCourse_id());
        study.setUser_id(UserService.getInstance().user_id);
        StudyService.getInstance().requestCourseProgress(study,handler);

        ExamService examService = ExamService.getInstance();
        examService.requestExams(course.getCourse_id(),handler);  //获取试题列表

        super.onResume();
    }


    //加载科目目录
    public void loadMulu(List<String> list){
        muludatas.clear();
        muludatas.addAll(list);
        muluAdapter.notifyDataSetChanged();  //更新数据
    }

    //加载课程介绍
    public void loadIntroduction(String teachername, String introduction){
        teacherName.setText(teachername);
        courseIntrodction.setText(introduction);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_jianjie_xuexi:  //简介
                muluContent.setVisibility(View.GONE);
                jianjieContent.setVisibility(View.VISIBLE);
                break;

            case R.id.button_mulu_xuexi:  //目录
                muluContent.setVisibility(View.VISIBLE);
                jianjieContent.setVisibility(View.GONE );
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent == mululist){
            Intent intent = new Intent(KemuActivity.this,KewenneirongActivity.class); //跳转到内容界面
            intent.putExtra("index",position);
            intent.putExtra("count",count);
            startActivity(intent);
        }
    }
}
