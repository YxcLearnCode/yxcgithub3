package com.example.hucx.myapplication;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.service.CourseService;

import java.util.ArrayList;
import java.util.List;

public class CuotibenActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


     // 定义
    private List<String> cuotikemudatas = new ArrayList<>();
    private ArrayAdapter<String> cuotikemuAdapter;
    private CourseService courseService;
    private List<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cuotiben);

        courseService = CourseService.getInstance();
        courses = courseService.getStudycourseList();
        for(Course course:courses){
            cuotikemudatas.add(course.getCourse_name());  //获取科目名字列表
        }

        //错题科目列表
        ListView cuotikemulist = (ListView) findViewById(R.id.list_cuotikemu_cuotiben);
        cuotikemuAdapter = new ArrayAdapter<String>( CuotibenActivity.this, android.R.layout.simple_list_item_1, cuotikemudatas);
        cuotikemulist.setAdapter(cuotikemuAdapter);
        cuotikemulist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(CuotibenActivity.this,CuotiActivity.class);
        intent.putExtra("courseid",courses.get(position).getCourse_id());
        startActivity(intent);
    }

}
