package com.example.hucx.myapplication;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.beans.Answer;
import com.example.hucx.myapplication.beans.Topic;
import com.example.hucx.myapplication.service.ExamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ShitiJiemianActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView countDown;
    private TextView question;
    private RadioButton option_a;
    private RadioButton option_b;
    private RadioButton option_c;
    private RadioButton option_d;
    private EditText answer;
    private Button next;
    private TextView submit;


    private List<Topic> topicList;  //题目列表
    private List<Answer> answers = new ArrayList<>(); //用户答案
    private int result;//考试成绩
    private ExamService examService;
    private int index = 0;
    final Timer timer = new Timer();


    private Handler handler = new Handler(){
        int time = 45*60;
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    String str = "剩余时间 "+ time/60 + ":" +time%60;
                    countDown.setText(str);
                    if(time == 0){
                        timer.cancel();
                    }
                    time--;
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_shiti_jiemian);

        countDown = findViewById(R.id.textView_countDown_shitijiemian);
        question = findViewById(R.id.textView_question_shitijiemian);
        option_a = findViewById(R.id.radiobtn_a_shitijiemian);
        option_b = findViewById(R.id.radiobtn_b_shitijiemian);
        option_c = findViewById(R.id.radiobtn_c_shitijiemian);
        option_d = findViewById(R.id.radiobtn_d_shitijiemian);
        answer = findViewById(R.id.edittext_answer_shitijiemian);
        submit = findViewById(R.id.textView_submit_shitijiemian);
        next = findViewById(R.id.btn_next_shitijiemian);

        findViewById(R.id.imageView_fanhui_shitijiemian).setOnClickListener(this);

        next.setOnClickListener(this);
        submit.setOnClickListener(this);

        examService = ExamService.getInstance();

        topicList = examService.getTopics();
        countDown();
        if(null != topicList){
            updateTopic();

        }

    }

    private void countDown(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1);

            }
        }, 0, 1000);
    }

    private void updateTopic()
    {
        question.setText(index + 1 + "、" + topicList.get(index).getQuestion());
        RadioGroup radioGroup = findViewById(R.id.radioGroup_option_shitijiemian);
        Topic topic = topicList.get(index);

        if(0 == topic.getType() || 2 == topic.getType()) {
            answer.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
        }else if(1 == topic.getType()) {
            answer.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.clearCheck();

            String[] options = {topic.getOption_a(), topic.getOption_b(), topic.getOption_c(), topic.getOption_d()};
            RadioButton[] radioButtons = {option_a, option_b, option_c, option_d};

            for (RadioButton radioButton : radioButtons) {
                radioButton.setVisibility(View.GONE);
            }
            for (int i = 0; i < options.length; i++) {
                if (null != options[i] && "" != options[i]) {
                    radioButtons[i].setVisibility(View.VISIBLE);
                    radioButtons[i].setText(options[i]);
                }
            }
        }
    }

    //提交错题
    private void submit(){
        timer.cancel();
        AlertUtils.Alert(ShitiJiemianActivity.this,"测试成绩","本次测试成绩为"+ result + "分","上传试题",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
               examService.requestSubmitAnswer(answers,handler);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.textView_submit_shitijiemian)
        {
            if(index == topicList.size()-1) {
                submit();
            }else{
                AlertUtils.Alert(ShitiJiemianActivity.this,"未写完所有题目！",null);
            }
        }else if(id == R.id.imageView_fanhui_shitijiemian){
            finish();
        }else if(id == R.id.btn_next_shitijiemian)
        {
            Topic bean = topicList.get(index);
            Answer topicanswer = new Answer();
            topicanswer.setExam_id(examService.getExamId());
            topicanswer.setTopic_id(bean.getId());
            topicanswer.setIserror(1);

            if(0 == bean.getType()) { //填空题判断
                String str = answer.getText().toString().trim();
                if(null != str && !"".equals(str)) {
                    topicanswer.setAnswer(str);
                    if (bean.getOption_a().equals(str)) {
                        topicanswer.setIserror(0);
                        result += 2;
                    }
                    answers.add(topicanswer);
                    answer.setText("");
                }else Toast.makeText(ShitiJiemianActivity.this, "请输入答案", Toast.LENGTH_SHORT).show();

            }else if(1 == bean.getType()) {  //选择题判断
                int choose = -1;
                if (option_a.isChecked()) {
                    choose = 0;
                } else if (option_b.isChecked()) {
                    choose = 1;
                } else if (option_c.isChecked()) {
                    choose = 2;
                } else if (option_d.isChecked()) {
                    choose = 3;
                } else Toast.makeText(ShitiJiemianActivity.this, "请选择", Toast.LENGTH_SHORT).show();

                if (-1 != choose) {
                    topicanswer.setAnswer(String.valueOf(choose));
                    if (bean.getAnswer().equals(String.valueOf(choose))) {
                        topicanswer.setIserror(0);
                        result += 2;
                    }
                    answers.add(topicanswer);
                }
            }else if(2 == bean.getType()){  //简答题判定
                String str = answer.getText().toString().trim();
                if(null != str && !"".equals(str)) {
                    topicanswer.setAnswer(str);
                    if (str.contains(bean.getOption_a()) && str.contains(bean.getOption_b()) && str.contains(bean.getOption_c())) {
                        topicanswer.setIserror(0);
                        result += 30;
                    }else if(str.contains(bean.getOption_a())){
                        result += 10;
                    }else if(str.contains(bean.getOption_b())){
                        result += 10;
                    }else if(str.contains(bean.getOption_c())){
                        result += 10;
                    }
                    answers.add(topicanswer);
                    answer.setText("");
                }else Toast.makeText(ShitiJiemianActivity.this, "请输入答案", Toast.LENGTH_SHORT).show();
            }

            if(index == topicList.size()-1){
                next.setText("提交");
                AlertUtils.Alert(ShitiJiemianActivity.this,"交卷","确认提交试题？","交卷",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submit();
                    }
                });
            }else{
                if(null != topicanswer.getAnswer()) {
                    index++;
                    updateTopic();
                }
            }
        }
    }
}
