package com.example.hucx.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.beans.Plan;
import com.example.hucx.myapplication.service.CourseService;
import com.example.hucx.myapplication.service.StudyService;
import com.example.hucx.myapplication.service.UserService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ZhidingjihuaActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{


    //制定计划 科目下拉框
    private Spinner zhidingjihuaspinner;      //制定计划 下拉框
    private List<String> kemudatas; //下拉列表的数据
    private ArrayAdapter<String> kemuAdapter;  //科目下拉列表适配器

    private ImageView fanhui;
    private Button time;
    private TextView submit;

    private CourseService courseService;
    private StudyService studyService;
    private Plan plan;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_zhidingjihua_);

        courseService = CourseService.getInstance();
        studyService = StudyService.getInstance();

        zhidingjihuaspinner = (Spinner) findViewById(R.id.spinner_kemu_zhidingjihua);

        courses = courseService.getStudycourseList();
        kemudatas = new ArrayList<>();
        for(Course course:courses) {
            kemudatas.add(course.getCourse_name());
        }
        kemuAdapter = new ArrayAdapter<String>( ZhidingjihuaActivity.this, android.R.layout.simple_list_item_1, kemudatas);
        zhidingjihuaspinner.setAdapter(kemuAdapter);
        zhidingjihuaspinner.setOnItemSelectedListener(this);

        fanhui = findViewById(R.id.image_fanhui_zhidingjihua);
        fanhui.setOnClickListener(this);
        time = findViewById(R.id.btn_time_zhidingjihua);
        time.setOnClickListener(this);
        submit = findViewById(R.id.textVie_submit_zhidingjihua);
        submit.setOnClickListener(this);

        plan = new Plan();
        plan.setUser_id(UserService.getInstance().user_id);
        if(kemudatas.size()>0) {
            int course_id = courses.get(0).getCourse_id();
            plan.setCourse_id(course_id);
        }
    }

    private void chooseDate(){
        Calendar c = Calendar.getInstance();
        AlertUtils.datePickerDialog(ZhidingjihuaActivity.this, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //String date = String.format("%d-%d-%d", year, month+1, dayOfMonth);
                String date = String.format("%02d-",year) + (month< 10?String.format("%02d-",month+1):String.format("%d-",month+1)) +
                        (dayOfMonth< 10?String.format("%02d",dayOfMonth):String.format("%d",dayOfMonth));
                chooseTime(date);
            }
        });
    }

    private void chooseTime(final String date){
        Calendar c = Calendar.getInstance();
        AlertUtils.timePickerDialog(ZhidingjihuaActivity.this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int mounte) {
                String time1 = String.format("%d:%d", hour, mounte);
                String timeStr = (hour< 10?String.format("%02d:",hour):String.format("%d:",hour)) +
                        (mounte< 10?String.format("%02d",mounte):String.format("%d",mounte));
                time.setText(date + " "+timeStr);
                plan.setTime(date + " "+timeStr);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == fanhui){
            finish();
        }else if(v == time){
            chooseDate();
        }else if(v == submit){
            if(null == plan.getTime() || 16 > plan.getTime().length()){
                AlertUtils.Alert(ZhidingjihuaActivity.this, "请选择时间！", null);

            }else {
                studyService.requestAddPlan(plan,new Handler());
                AlertUtils.Alert(ZhidingjihuaActivity.this, "提示", "计划添加成功！", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int course_id = courses.get(position).getCourse_id();
        plan.setCourse_id(course_id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
