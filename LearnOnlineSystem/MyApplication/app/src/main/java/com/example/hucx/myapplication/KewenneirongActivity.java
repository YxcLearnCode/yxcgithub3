package com.example.hucx.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hucx.myapplication.beans.AnalysisData;
import com.example.hucx.myapplication.beans.CourseContent;
import com.example.hucx.myapplication.beans.Study;
import com.example.hucx.myapplication.service.CourseService;
import com.example.hucx.myapplication.service.StudyService;
import com.example.hucx.myapplication.service.UserService;

public class KewenneirongActivity extends AppCompatActivity {

    private CourseService courseService;
    private StudyService studyService;

    private int count;   //已学习 的章节数
    private int total; //全部章节数
    private CourseContent bean;  //内容

    final android.os.Handler handler = new android.os.Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String message = b.getString("msg");
            String request = b.getString("request");

            if("addprogress".equals(request)){
                if("true".equals(message)){
                    //如果是新章节更新学习进度
                    Study study = new Study();
                    study.setUser_id(UserService.getInstance().user_id);
                    study.setCourse_id(bean.getCourse_id());
                    study.setPercent((count+1)*100/total);
                    studyService.requestAddStydy(study,new Handler());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_kewenneirong);

        courseService = CourseService.getInstance();
        int index  = getIntent().getIntExtra("index",0);
        count = getIntent().getIntExtra("count",0);
        bean = courseService.getCourseText(Integer.valueOf(index));

        studyService = StudyService.getInstance();
        total = CourseService.getInstance().getContentList().size();
        //更新进度
        if(count < total){
            AnalysisData data = new AnalysisData();
            data.setUser_id(UserService.getInstance().user_id);
            data.setCourse_id(bean.getCourse_id());
            data.setContent_id(bean.getId());
            studyService.requestAddProgress(data,handler);
           //Snackbar.make(title,String.valueOf(((count * total/100)+1)*100/total),Snackbar.LENGTH_LONG).show();
        }


        TextView title  = findViewById(R.id.textView_title_kemuneirong); //获取标题控件 设置标题
        title.setText(bean.getContent_name());

        TextView content  = findViewById(R.id.textView_text_kewenneirong);  //获取内容控件 设置内容
        content.setText(bean.getText());
    }
}
