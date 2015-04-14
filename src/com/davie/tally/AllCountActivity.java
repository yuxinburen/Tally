package com.davie.tally;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.davie.adapter.ListViewAdapter;
import model.Detail;

import java.util.ArrayList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-13
 */
public class AllCountActivity extends Activity {
    private DatePicker startPicker;
    private DatePicker endPicker;
    private Spinner spinner_type;
    private EditText edt_remark;
    private Button btn_search;
    private ListView listView;

    private ListViewAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcount);

    }

    public void init(){
        startPicker = (DatePicker) findViewById(R.id.start_picker);
        endPicker = (DatePicker) findViewById(R.id.end_picker);
        spinner_type = (Spinner) findViewById(R.id.spinner_type);
        edt_remark = (EditText) findViewById(R.id.edt_remark);
        btn_search = (Button) findViewById(R.id.btn_search);
        listView = (ListView) findViewById(R.id.listview_allcount);

        adapter = new ListViewAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AllCountActivity.this);
                builder.setTitle("记录详情");
                View view1 = LayoutInflater.from(AllCountActivity.this).inflate(R.layout.item_detail,null);
                builder.setView(view1);
                builder.setPositiveButton("删除", null);
                builder.setNeutralButton("修改",null);
                builder.setNegativeButton("确定", null);
                builder.create().show();
            }
        });
    }

    public void loadViewDetail(View v){
        TextView remark_detail;
        TextView money_detail;
        TextView type_detail;
        TextView category_detail;
        TextView date_detail;
        TextView note_detail;
        remark_detail = (TextView) v.findViewById(R.id.remark_detail);
        money_detail = (TextView) v.findViewById(R.id.money_detail);
        type_detail = (TextView) v.findViewById(R.id.type_detail);
        category_detail = (TextView) v.findViewById(R.id.category_detail);
        date_detail = (TextView) v.findViewById(R.id.category_detail);
        note_detail = (TextView) v.findViewById(R.id.note_detail);

        List<Detail> list = new ArrayList<Detail>();
        



    }
}