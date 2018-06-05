package com.example.hucx.myapplication;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.beans.Topic;
import com.example.hucx.myapplication.service.CourseService;
import com.example.hucx.myapplication.service.ExamService;

import java.util.ArrayList;
import java.util.List;

public class CuotiActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView question;
    private RadioButton option_a;
    private RadioButton option_b;
    private RadioButton option_c;
    private RadioButton option_d;
    private TextView answer;
    private Button next;
    private ImageView fanhui;


    private List<Topic> errorTopicList = new ArrayList();
    private ExamService examService;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cuoti);

        question = findViewById(R.id.textView_question_cuoti);
        option_a = findViewById(R.id.radiobtn_a_cuoti);
        option_b = findViewById(R.id.radiobtn_b_cuoti);
        option_c = findViewById(R.id.radiobtn_c_cuoti);
        option_d = findViewById(R.id.radiobtn_d_cuoti);
        answer = findViewById(R.id.textview_answer_cuoti);
        next = findViewById(R.id.btn_next_cuoti);
        fanhui = findViewById(R.id.image_fanhui_cuoti);

        next.setOnClickListener(this);
        fanhui.setOnClickListener(this);

        int courseid = getIntent().getIntExtra("courseid",0);
        Course bean = CourseService.getInstance().getStudyCourse(courseid);
        examService = ExamService.getInstance();
        errorTopicList = examService.getErrorTopicsByCourse(bean.getCourse_id());
        if(errorTopicList.size()>0){
            updateTopic();
        }else{
            AlertUtils.Alert(CuotiActivity.this, "没有错题记录", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    //加载题目
    private void updateTopic(){
        Topic bean = errorTopicList.get(index);
        question.setText(index + 1 + "、" + bean.getQuestion());
        RadioGroup radioGroup = findViewById(R.id.radioGroup_option_cuoti);


        if(1 == bean.getType()) {
            answer.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            String[] options = {bean.getOption_a(), bean.getOption_b(), bean.getOption_c(), bean.getOption_d()};
            RadioButton[] radioButtons = {option_a, option_b, option_c, option_d};
            radioGroup.clearCheck();

            for (RadioButton radioButton : radioButtons) {
                radioButton.setTextColor(Color.parseColor("#000000"));
                radioButton.setVisibility(View.GONE);
            }
            for (int i = 0; i < options.length; i++) {
                if (null != options[i] && "" != options[i]) {
                    radioButtons[i].setVisibility(View.VISIBLE);
                    radioButtons[i].setText(options[i]);

                    if (String.valueOf(i).equals(bean.getAnswer())) {
                        radioGroup.check(radioButtons[i].getId());
                        radioButtons[i].setTextColor(Color.parseColor("#ff0000"));
                    }
                }
            }
        }else if(2 == bean.getType()){
            answer.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            answer.setText("关键词：" + bean.getOption_a() + "  " + bean.getOption_b() + "  "+ bean.getOption_c());
        }else{
            answer.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            answer.setText("答案为：" + bean.getOption_a());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.image_fanhui_cuoti){
            finish();
        }else if(id == R.id.btn_next_cuoti)
        {
            if(index == errorTopicList.size()-1){
                next.setClickable(false);
            }else {
                index++;
                updateTopic();
            }
        }
    }
}
