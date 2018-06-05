package com.example.hucx.myapplication.Utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;

/**
 * Created by hucx on 2018/3/2.
 */

public class AlertUtils {

    public static void Alert(Context context,String title,String message,String btnname,final DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的内容
        builder.setTitle(title);
        //    设置Content来显示一个信息
        builder.setMessage(message);
        //    设置一个PositiveButton
        builder.setPositiveButton(btnname, listener);
        //    设置一个NegativeButton
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        builder.show();
    }

    //提示框
    public static void Alert(Context context,String message,final DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //    设置Title的内容
        builder.setTitle("提示");
        //    设置Content来显示一个信息
        builder.setMessage(message);
        //    设置一个PositiveButton
        builder.setPositiveButton("确定", listener);
        //    设置一个NegativeButton
        builder.show();
    }

    //下拉框弹窗
    public static void Spinner(Context context,String title,final String[] strings, final DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        //    设置一个下拉的列表选择项
        builder.setItems(strings, listener);
        builder.show();
    }

    //日期选择器
    public static void datePickerDialog(Context context,int year,int month,int day,final DatePickerDialog.OnDateSetListener listener) {
        new DatePickerDialog(context, listener, year, month, day).show();
    }

    //时间选择器
    public static void timePickerDialog(Context context,int hour,int minute,final TimePickerDialog.OnTimeSetListener listener) {
        new TimePickerDialog(context, listener, hour, minute, true).show();
    }
}
