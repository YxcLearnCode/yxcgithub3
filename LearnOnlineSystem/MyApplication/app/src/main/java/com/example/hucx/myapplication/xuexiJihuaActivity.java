package com.example.hucx.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hucx.myapplication.Utils.AlertUtils;
import com.example.hucx.myapplication.Utils.JsonUtils;
import com.example.hucx.myapplication.beans.CourseContent;
import com.example.hucx.myapplication.beans.Plan;
import com.example.hucx.myapplication.service.StudyService;

import java.util.ArrayList;
import java.util.List;

public class xuexiJihuaActivity extends AppCompatActivity implements View.OnClickListener{

    // 定义
    private FloatingActionButton tianjiajihuaBtn;
    private ListView jihualist;      //计划列表
    final private List<String> jihuadatas = new ArrayList<>();

    private ArrayAdapter<String> jihuaAdapter;
    private StudyService studyService;

    final android.os.Handler handler = new android.os.Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String message = b.getString("msg");
            String request = b.getString("request");

            if("请求失败".equals(message)){
                AlertUtils.Alert(xuexiJihuaActivity.this,"无网络连接！",null);
            }
            if("getplan".equals(request)) {
                List<Plan> plans = JsonUtils.JsonToList(message,Plan.class);
                studyService.setPlanBean(plans);
                jihuadatas.clear();
                jihuadatas.addAll(studyService.getPlanBean());
                jihuaAdapter.notifyDataSetChanged();
            }
            //Snackbar.make(xuexiContent,message,Snackbar.LENGTH_LONG).show();
        }
    };

    //初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_xuexijihua);

        tianjiajihuaBtn = (FloatingActionButton) findViewById(R.id.btn_tianjiajihua_xuexijihua);
        tianjiajihuaBtn.setOnClickListener(this);

        //计划列表
        jihualist=(ListView) findViewById(R.id.list_jihua_xuexijihua);
        jihuaAdapter = new ArrayAdapter<String>( xuexiJihuaActivity.this, android.R.layout.simple_list_item_1, jihuadatas);
        jihualist.setAdapter(jihuaAdapter);

        // 添加长按点击弹出选择菜单
        jihualist.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
            }
        });

        studyService = StudyService.getInstance();
    }

    @Override
    protected void onResume() {
        studyService.requestGetPlan(handler);
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long position = info.id;
        switch (item.getItemId()) {
            case 0:
                //Toast.makeText(xuexiJihuaActivity.this,String.valueOf(position),Toast.LENGTH_SHORT).show();
                jihuadatas.remove((int)position);
                jihuaAdapter.notifyDataSetChanged();
                studyService.requestDelPlan((int)position,handler);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //设置点击响应
    @Override
    public void onClick(View v) {   //点击响应
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_tianjiajihua_xuexijihua:  //学习
                intent = new Intent(xuexiJihuaActivity.this,ZhidingjihuaActivity.class);
                startActivity(intent);
                break;

        }
    }
}
