package com.davie.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.davie.tally.R;
import com.davie.utils.DateUtils;
import com.davie.utils.MySQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewMenuAdapter extends BaseAdapter {

    private static final String TAG = "ListViewAdapter";
    private Context context;
    private List<Map<String, Object>> list;

    public List<Map<String, Object>> getList(){
        return list;
    }

    //数据库帮助类
    MySQLiteOpenHelper helper;

    public void loadDataCategory(String type_id){
        String sql = " select category name from tb_category where type_id = ? ";
        Cursor cursor = helper.selectCursor(sql,new String[]{type_id});
        List<Map<String,Object>> data = helper.cursorToList(cursor);
        list.addAll(data);
    }

    public void loadDataTpye(){
        String sql = " select type name from tb_type where _id > 1 ";
        Cursor cursor = helper.selectCursor(sql,null);
        List<Map<String,Object>> data = helper.cursorToList(cursor);
        list.addAll(data);
    }

    public ListViewMenuAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<Map<String, Object>>();
        helper = new MySQLiteOpenHelper(context);
    }

    public void add(List<Map<String, Object>> data) {
        list.addAll(data);
        Log.e(TAG, ">>>>>>>>>>>" + list.size());
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
                    R.layout.item_listview_menu, null);
            viewHolder.name_item_listview_menu = (TextView) convertView
                    .findViewById(R.id.name_item_listview_menu);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name_item_listview_menu.setText(list.get(position).get("name")
                .toString());
        return convertView;
    }

    class ViewHolder {
        TextView name_item_listview_menu;//类型:收入或者支出
    }
}
