package com.davie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.davie.tally.R;
import com.davie.utils.DateUtils;
import com.davie.utils.MySQLiteOpenHelper;
import com.lidroid.xutils.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewAdapter extends BaseAdapter {

    private static final String TAG = "ListViewAdapter";
    private Context context;
    private List<Map<String, Object>> list;

    //数据库帮助类
    MySQLiteOpenHelper helper;

    public void loadData(){
        //获取当前日期
        int [] date = DateUtils.getCurrentDate();

        String sql = " select type_id type, money, category_id category, note, dt, tm, _id from tb_detail where dt = ? order by _id desc ";
        Cursor cursor = helper.selectCursor(sql,new String[]{date[0]+"-"+date[1]+"-"+date[2]});
        List<Map<String,Object>> data = helper.cursorToList(cursor);
        list.addAll(data);
    }

    public ListViewAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<Map<String, Object>>();
        helper = new MySQLiteOpenHelper(context);
    }

    public void add(List<Map<String, Object>> data) {
        list.addAll(data);
        Log.e(TAG, ">>>>>>>>>>>" + list.size());
    }

    public void reload(){
        list.clear();
        loadData();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_listview_main, null);
            viewHolder.type_item = (TextView) convertView
                    .findViewById(R.id.type_item);
            viewHolder.money_item = (TextView) convertView
                    .findViewById(R.id.money_item);
            viewHolder.category_item = (TextView) convertView
                    .findViewById(R.id.category_item);
            viewHolder.note_item = (TextView) convertView
                    .findViewById(R.id.note_item);
            viewHolder.date_item = (TextView) convertView
                    .findViewById(R.id.date_item);
            viewHolder.time_item = (TextView) convertView
                    .findViewById(R.id.time_item);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.type_item.setText(list.get(position).get("type")
                .toString()+"：");
        viewHolder.money_item.setText(list.get(position).get("money")
                .toString());
        viewHolder.category_item.setText(list.get(position).get("category")
                .toString());
        viewHolder.note_item.setText(list.get(position).get("note")
                .toString());
        viewHolder.date_item.setText(list.get(position).get("dt")
                .toString());
        viewHolder.time_item.setText(list.get(position).get("tm")
                .toString());
        return convertView;
    }

    class ViewHolder {
        TextView type_item;//类型:收入或者支出
        TextView money_item;//数额
        TextView category_item;//所属种类
        TextView note_item;//备注
        TextView date_item;//日期
        TextView time_item;//时间
    }
}