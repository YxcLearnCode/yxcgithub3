package com.example.hucx.myapplication;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hucx.myapplication.beans.Course;
import com.example.hucx.myapplication.service.CourseService;


public class ListViewAdapter extends BaseAdapter {

    private List<Course> list = null;

    private LayoutInflater myLayoutInflater = null;
    private Context context = null;

    private static class ViewHolder{    //创建一个内部类ViewHolder，设置选项布局中的元素
        public TextView teach = null;
        public TextView name = null;
    }


    public ListViewAdapter(List<Course> list,Context context) {  //传入Adapter的参数，并创建Adapter
        this.context = context;
        this.list = list;
        this.myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Course item = list.get(position);
        //列表的Item
        ViewHolder holder = new ViewHolder();
        view = myLayoutInflater.inflate(R.layout.item_course_layout, null);

        //老师名字
        holder.teach = (TextView)view.findViewById(R.id.textview_teach_courselist);
        //列课程名字
        holder.name =(TextView) view.findViewById(R.id.textview_coursename_courselist);//更多  按钮

        holder.teach.setText(list.get(position).getCourse_teacher());
        holder.name.setText(list.get(position).getCourse_name());
        final int courseid = item.getCourse_id();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {             //更多按钮点击事件
                Intent intent = new Intent(context,KemuActivity.class); //跳转到kemu界面
                intent.putExtra("courseid",courseid);
                intent.putExtra("flag",0);
                context.startActivity(intent);
            }
        });
        return view;
    }
}