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
import com.davie.utils.MySQLiteOpenHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnRadioGroupCheckedChange;

import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;

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

    //数据库
    private MySQLiteOpenHelper helper;

    //对话框
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;

    //单选按钮的选择对象
    private int radioChoice;

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
        helper = new MySQLiteOpenHelper(this);
        radioChoice = radiogroup_main.getCheckedRadioButtonId();
        listView_today.setEmptyView(textview_empty);
        adapter = new ListViewAdapter(this);
        adapter.loadData();
        listView_today.setAdapter(adapter);

        final int[] date = DateUtils.getCurrentDate();
        button_date.setText(date[0] + "-" + date[1] + "-" + date[2]);
        int[] time = DateUtils.getCurrentTime();
        button_time.setText(time[0] + ":" + time[1]);

        builder = new AlertDialog.Builder(this);
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
    @OnClick({R.id.button_count,R.id.button_type, R.id.button_category, R.id.button_date, R.id.button_time, R.id.button_history, R.id.button_save})
    public void onclick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.button_count:
                intent.setClass(this,AccountActivity.class);
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
                intent.setClass(this,AccountActivity.class);
                startActivity(intent);
                break;
            case R.id.button_save:
                String money = editText_money.getText().toString();
                if (money != null) {
                    if (addData()) {
                        editText_money.setText("");
                        editText_note.setText("");
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        adapter.reload();
                    }
                }
                break;
        }
    }

    private List<Map<String, Object>> data;

    //选择类型
    public void choiceType() {
        builder.setTitle("选择类型");
        radioChoice = radiogroup_main.getCheckedRadioButtonId();
        if (radioChoice == R.id.radio_in) {
            button_type.setText("收入");
        } else {
            View view = getLayoutInflater().inflate(R.layout.item_menu_out_category, null);
            ListView listView_type = (ListView) view.findViewById(R.id.listview_menu_out);
            ListViewMenuAdapter menuAdapter = new ListViewMenuAdapter(this);
            menuAdapter.loadDataTpye();
            data = menuAdapter.getList();
            listView_type.setAdapter(menuAdapter);
            listView_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = data.get(i).get("name").toString();
                    button_type.setText(name);
                    if("衣服".equals(name)){
                        button_category.setText("自己穿");
                    }else if("饮食".equals(name)){
                        button_category.setText("三餐");
                    }else if("住宿".equals(name)){
                        button_category.setText("房租");
                    }else if("交通".equals(name)){
                        button_category.setText("公共");
                    }else if("生活".equals(name)){
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
        data = menuAdapter.getList();
        listView_menu_out.setAdapter(menuAdapter);
        listView_menu_out.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                button_category.setText(data.get(position).get("name").toString());
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
        }, date[0], date[1]-1, date[2]);
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
        String type = button_type.getText().toString();
        String category = button_category.getText().toString();
        String dt = button_date.getText().toString();
        String tm = button_time.getText().toString();
        String note = editText_note.getText().toString();
        String sql = " insert into tb_detail(type_id,category_id,money,note,dt,tm) values(?,?,?,?,?,?)";

        if(money=="null"||money.equals("")||money==null){
            Toast.makeText(this,"请输入数据后再保存", Toast.LENGTH_SHORT).show();
            return  false;
        }else {
            String da = type.equals("收入") ? money+"" : "-"+money;
            boolean flag = helper.execData(sql, new String[]{type, category, da, note, dt, tm});
            return flag;
        }
    }
}
