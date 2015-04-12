package com.davie.tally;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.davie.adapter.ListViewAdapter;
import com.davie.adapter.ListViewMenuAdapter;
import com.davie.utils.DateUtils;
import com.davie.utils.DbUtilsHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;
import model.Category;
import model.Detail;
import model.Type;
import org.apache.http.impl.client.DefaultTargetAuthenticationHandler;

import java.io.DataOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {

    @ViewInject(R.id.radiogroup_main)
    private RadioGroup radiogroup_main;
    @ViewInject(R.id.radio_in)
    private RadioButton radioButton_in;
    @ViewInject(R.id.radio_out)
    private RadioButton radioButton_out;

    @ViewInject(R.id.button_count)
    private Button button_count; //统计

    @ViewInject(R.id.editText_money)
    private EditText editText_money;//金额

    @ViewInject(R.id.editText_note)
    private EditText editText_note;//备注

    @ViewInject(R.id.button_type)
    private Button button_type;//类别 ：衣食住行

    @ViewInject(R.id.button_category)
    private Button button_category;// 种类: 工资 、外快 等

    @ViewInject(R.id.button_date)
    private Button button_date;//日期

    @ViewInject(R.id.button_time)
    private Button button_time;//时间

    @ViewInject(R.id.button_history)
    private Button button_history;//历史按钮

    @ViewInject(R.id.button_save)
    private Button button_save;//保存按钮

    @ViewInject(R.id.listview_today)
    private ListView listView_today;//今天的消费记录

    @ViewInject(R.id.textview_empty)
    private TextView textview_empty;//暂无记录

    //listView适配器
    private ListViewAdapter adapter;

    //对话框
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    //单选按钮的选择对象
    private int radioChoice;

    private DbUtilsHelper dbUtilsHelper;

    //选择菜单对象
    private List<Type> typeList;
    private List<Category> categoryList;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ViewUtils.inject(this);
        init();
    }

    public void init() {
        //单选组
        radioChoice = radiogroup_main.getCheckedRadioButtonId();
        //数据库工具类
        dbUtilsHelper = DbUtilsHelper.getInstance(this);
        //弹出对话框
        builder = new AlertDialog.Builder(this);

        //选择菜单:type和category
        categoryList = new ArrayList<Category>();
        typeList = new ArrayList<Type>();


        //今日收支情况
        adapter = new ListViewAdapter(this);
        listView_today.setEmptyView(textview_empty);
        listView_today.setAdapter(adapter);

        //初始化界面
        clear();
    }

    @OnRadioGroupCheckedChange(R.id.radiogroup_main)
    public void onRadioChecked(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_in:
                button_type.setText("收入");
                button_category.setText("工资");
                break;
            case R.id.radio_out:
                button_type.setText("衣服");
                button_category.setText("自己穿");
                break;
        }
    }

    //历史按钮和保存按钮
    @OnClick({R.id.button_count, R.id.button_type, R.id.button_category, R.id.button_date, R.id.button_time, R.id.button_history, R.id.button_save})
    public void onclick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_count:
                intent.setClass(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.button_type:
                choiceType();
                break;
            case R.id.button_category:
                choiceCategory();
                break;
            case R.id.button_date:
                choiceDate();
                break;
            case R.id.button_time:
                choiceTime();
                break;
            case R.id.button_history:
                intent.setClass(this, AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.button_save:
                addData();
                break;
        }
    }

    //选择类型
    public void choiceType() {
        builder.setTitle("选择类型");
        radioChoice = radiogroup_main.getCheckedRadioButtonId();
        if (radioChoice == R.id.radio_in) {
            button_type.setText("收入");
        } else {
            View view = getLayoutInflater().inflate(R.layout.item_menu_out_category, null);
            ListView listView_type = (ListView) view.findViewById(R.id.listview_menu_out);
            ListViewMenuAdapter<Type> menuAdapter = new ListViewMenuAdapter<Type>(this);
            menuAdapter.loadDataTpye();
            typeList = menuAdapter.getList();
            listView_type.setAdapter(menuAdapter);
            listView_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = typeList.get(i).getName();
                    button_type.setText(name);
                    if ("衣服".equals(name)) {
                        button_category.setText("自己穿");
                    } else if ("饮食".equals(name)) {
                        button_category.setText("三餐");
                    } else if ("住宿".equals(name)) {
                        button_category.setText("房租");
                    } else if ("交通".equals(name)) {
                        button_category.setText("公共");
                    } else if ("生活".equals(name)) {
                        button_category.setText("娱乐");
                    }
                    alertDialog.cancel();
                }
            });
            builder.setView(view);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }


    //选择明细
    public void choiceCategory() {
        View menu = getLayoutInflater().inflate(R.layout.item_menu_out_category, null);
        ListView listView_menu_out = (ListView) menu.findViewById(R.id.listview_menu_out);
        builder.setTitle("选择明细");
        String type = button_type.getText().toString();
        ListViewMenuAdapter menuAdapter = new ListViewMenuAdapter(this);
        menuAdapter.loadDataCategory(type);
        categoryList = menuAdapter.getList();
        listView_menu_out.setAdapter(menuAdapter);
        listView_menu_out.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = categoryList.get(position).getName();
                button_category.setText(name);
                alertDialog.cancel();
            }
        });
        builder.setView(menu);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void choiceDate() {
        int[] date = DateUtils.getCurrentDate();
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                button_date.setText(i + "-" + i1 + "-" + i2);
            }
        }, date[0], date[1] - 1, date[2]);
        datePicker.show();
    }

    //时间选择器
    public void choiceTime() {
        int[] time = DateUtils.getCurrentTime();
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                button_time.setText(i + ":" + i1);
            }
        }, time[0], time[1], true);
        timePicker.show();
    }

    //添加一条数据
    public boolean addData() {
        String money = editText_money.getText().toString();
        if (money != null) {
            String type = button_type.getText().toString();
            String category = button_category.getText().toString();
            String dt = button_date.getText().toString();
            String tm = button_time.getText().toString();
            String note = editText_note.getText().toString();
            float  num = type.equals("收入") ? Float.parseFloat(money)  : Float.parseFloat("-"+money);

            Detail detail = new Detail();
            Type type1 = dbUtilsHelper.getType(type);
            Category category1 = dbUtilsHelper.getCategory(category);
            detail.setType(type1);
            detail.setCategory(category1);
            detail.setMoney(num);
            detail.setDate(dt);
            detail.setNote(note);
            if(num > 0 )detail.setRemark(1);
            else detail.setRemark(2);
            boolean flag = dbUtilsHelper.save(detail);
            if (flag) {
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                clear();
                adapter.reload();
                return true;
            }
            return false;
        }
        Toast.makeText(this, "请输入数据后再保存", Toast.LENGTH_SHORT).show();
        return false;
    }

    public void clear(){
        editText_money.setText("");
        editText_note.setText("");
        button_type.setText("衣服");
        button_category.setText("自己穿");
        final int[] date = DateUtils.getCurrentDate();
        button_date.setText(date[0] + "-" + date[1] + "-" + date[2]);
        int[] time = DateUtils.getCurrentTime();
        button_time.setText(time[0] + ":" + time[1]);
    }
}
