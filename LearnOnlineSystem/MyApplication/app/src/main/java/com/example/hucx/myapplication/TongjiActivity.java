package com.example.hucx.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.hucx.myapplication.service.StudyService;

import java.util.ArrayList;
import java.util.List;

public class TongjiActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView tongjilist;  //统计列表
    private LinearLayout btn_yixue;//  已学按钮
    private LinearLayout btn_weixue; //未学习按钮
    private ImageView fanhui; //返回按钮

    private List<String> datas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private StudyService studyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tongji);

        fanhui = findViewById(R.id.image_fanhui_tongji);
        btn_yixue = findViewById(R.id.textView_yixue_tongji);
        btn_weixue = findViewById(R.id.textView_weixue_tongji);
        tongjilist = findViewById(R.id.list_kemu_tongji);

        fanhui.setOnClickListener(this);
        btn_yixue.setOnClickListener(this);
        btn_weixue.setOnClickListener(this);

        adapter = new ArrayAdapter<String>( TongjiActivity.this, android.R.layout.simple_list_item_1, datas);
        tongjilist.setAdapter(adapter);

        studyService = StudyService.getInstance();
        //studyService.setLearned(TongjiActivity.this);
        //datas.addAll(studyService.yixuelist);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v == fanhui){  //返回
            finish();
        }else if(v.equals(btn_yixue)){
            //datas.clear();
            //datas.addAll(studyService.yixuelist);
            adapter.notifyDataSetChanged();

        }else if(v.equals(btn_weixue)){
            datas.clear();
            //datas.addAll(studyService.weixuelist);
            adapter.notifyDataSetChanged();
        }
    }
}
