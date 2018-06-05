package com.example.hucx.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.service.StudyService;

import java.util.ArrayList;
import java.util.List;

public class TongzhiActivity extends AppCompatActivity implements View.OnClickListener {

   //定义
    private ListView xiaoxilist;      //提醒列表
    final private List<String> remindBeans = new ArrayList<>();  //提醒数据源
    private ArrayAdapter xiaoxiAdapter;  //提醒列表适配器

    private StudyService studyService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tongzhi);

        //通知列表
        xiaoxilist=(ListView) findViewById(R.id.list_xiaoxi_tongzhi);
        xiaoxiAdapter = new ArrayAdapter(TongzhiActivity.this,android.R.layout.simple_list_item_1,remindBeans);
        xiaoxilist.setAdapter(xiaoxiAdapter);

        findViewById(R.id.image_fanhui_tongzhi).setOnClickListener(this);
        findViewById(R.id.image_shanchu_tongzhi).setOnClickListener(this);

        studyService = StudyService.getInstance();

        remindBeans.addAll(studyService.getRemindBean());
        xiaoxiAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.image_fanhui_tongzhi:  //返回
                finish();
                break;
            case R.id.image_shanchu_tongzhi:  //清空
                AlertUtils.Alert(TongzhiActivity.this, "删除提示", "确定删除所有提醒？", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remindBeans.clear();
                        xiaoxiAdapter.notifyDataSetChanged();
                        //studyService.removeAllRemindBean(TongzhiActivity.this);
                    }
                });
                break;
        }
    }
}

