package com.davie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.davie.tally.R;
import com.davie.utils.DbUtilsHelper;
import model.Base;
import model.Category;
import model.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewMenuAdapter<T> extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";
    private DbUtilsHelper dbUtilsHelper;
    private Context context;
    private  List<T> list;

    public ListViewMenuAdapter(Context context) {
        super();
        if (context == null) {
            throw new IllegalArgumentException(" The context must not null ");
        }
        this.context = context;
        list = new ArrayList<T>();
        dbUtilsHelper = DbUtilsHelper.getInstance(context);
    }

    public List<T> getList(){
        return list;
    }

    public void loadDataCategory(String type_id){
        for (Category category : dbUtilsHelper.loadCategory()) {
            T t = (T)category;
            list.add(t);
        }
    }

    public void loadDataTpye(){
        list.clear();
        for (Type type : dbUtilsHelper.loadType()) {
            T t = (T)type;
            list.add(t);
        }
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

        viewHolder.name_item_listview_menu.setText(((Base)list.get(position)).getName());
        return convertView;
    }

    private static class ViewHolder {
        private TextView name_item_listview_menu;//类型:收入或者支出
    }
}
