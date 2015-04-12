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
import com.davie.utils.DbUtilsHelper;
import com.davie.utils.MySQLiteOpenHelper;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import model.Detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";
    private DbUtilsHelper dbUtilsHelper;
    private List<Detail> list;
    private Context context;

    public void toDayData(){
        List<Detail> data = dbUtilsHelper.loadToday();
        if(data!=null){
            list.addAll(data);
        }
//        list.addAll(dbUtilsHelper.loadToday());
    }
    public ListViewAdapter(Context context) {
        super();
        if (context == null) {
            throw new IllegalArgumentException(" The context must not null ");
        }
        this.context = context;
        list = new ArrayList<Detail>();
        dbUtilsHelper = DbUtilsHelper.getInstance(context);

        toDayData();
    }

    public void add(List<Detail> data) {
        list.addAll(data);
        Log.e(TAG, ">>>>>>>>>>>" + list.size());
    }

    public void reload(){
        list.clear();
        toDayData();
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

        viewHolder.type_item.setText(list.get(position).getType().getName()+"：");
        viewHolder.money_item.setText(list.get(position).getMoney()+"");
        viewHolder.category_item.setText(list.get(position).getCategory().getName());
        viewHolder.note_item.setText(list.get(position).getNote());
        viewHolder.date_item.setText(list.get(position).getDate()+"");
//        viewHolder.time_item.setText(list.get(position).getDate()+"");
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