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

import java.util.*;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewSurveyAdapter extends BaseAdapter {

    private static final String TAG = "ListViewSurveyAdapter";
    private Context context;
    private List<Map<String, Object>> list;

    //数据库帮助类
    private MySQLiteOpenHelper helper;

    public void loadData() {
        //获取当前日期
        int[] date = DateUtils.getCurrentDate();

        String sql = " select type_id type, money, category_id category, note, dt, tm from tb_detail where dt = ? ";
        Cursor cursor = helper.selectCursor(sql, new String[]{date[0] + "-" + date[1] + "-" + date[2]});
        List<Map<String, Object>> data = helper.cursorToList(cursor);
        list.addAll(data);


    }

    public void loadThisWeek(){
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int startDay = day-dayOfWeek;
    }

    /**
     * 今天的结余
     */
    public void loadToday(){
        int [] date = DateUtils.getCurrentDate();
        String start = date[0]+"-"+date[1]+"-"+date[2];
        String end = date[0]+"-"+date[1]+"-"+date[2];
        int type = 1;
        queryData(type,start,end);
    }
    //计算结余
    public void queryData(int type, String start, String end) {

        Map<String, Object> map = new HashMap<String, Object>();
        switch (type){
            case 1:
                map.put("title","今日结余:");
                break;
            case 2:
                map.put("title","昨日结余:");
                break;
            case 3:
                map.put("title","本周结余:");
                break;
            case 4:
                map.put("title","上周结余:");
                break;
            case 5:
                map.put("title","本月结余:");
                break;
            case 6:
                map.put("title","上月结余:");
                break;
            case 7:
                map.put("title","今年结余:");
                break;
            case 8:
                map.put("title","去年结余:");
                break;
            case 9:
                map.put("title","全部结余:");
                break;
        }
        String sqlsum = " select sum(money) sum, count(_id) count from tb_detail where dt >= ? and dt <= ? ";
        Cursor sum = helper.selectCursor(sqlsum, new String[]{start, end});
        while (sum.moveToNext()) {
            map.put("sum",sum.getString(0)==null?"0.0":sum.getString(0));
            map.put("count","("+sum.getString(1)+")");
        }
        sum.close();

        String sqlInput = " select sum(money) numberInput, count(_id) countInput from tb_detail where dt >= ? and dt <= ? and ( type_id like '工资'  or type_id like '外快') ";
        Cursor cursorInput = helper.selectCursor(sqlInput, new String[]{start, end});
//        map.put("numberInput", "0.0");
//        map.put("countInput", "(0)");
        while (cursorInput.moveToNext()) {
            map.put("numberInput", cursorInput.getString(0)==null?"0.0":cursorInput.getString(0));
            map.put("countInput", "(" + cursorInput.getString(1) + ")");
        }
        cursorInput.close();

        String sqlOutput = " select sum(money) numberOutput, count(_id) countOutput from tb_detail where dt >= ? and dt <= ? and (type_id not like '工资' and type_id not like '外快') ";
        Cursor cursorOutput = helper.selectCursor(sqlOutput, new String[]{start, end});
//        map.put("numberOutput", "0.0");
//        map.put("countOutput", "(0)");
        while (cursorOutput.moveToNext()) {
            map.put("numberOutput", cursorOutput.getString(0)==null?"0.0":cursorOutput.getString(0)+"");
            map.put("countOutput", "(" + cursorOutput.getString(1) + ")");
        }
        cursorOutput.close();

        map.put("titleInput", "收入：");
        map.put("titleOutput", "支出：");

        list.add(map);
    }

    public ListViewSurveyAdapter(Context context) {
        super();
        this.context = context;
        list = new ArrayList<Map<String, Object>>();
        helper = new MySQLiteOpenHelper(context);
        loadToday();
    }

    public void add(List<Map<String, Object>> data) {
        list.addAll(data);
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
                    R.layout.item_listview_survey, null);

            viewHolder.title_item_survey = (TextView) convertView
                    .findViewById(R.id.title_item_survey);
            viewHolder.sum_item_survey = (TextView) convertView
                    .findViewById(R.id.sum_item_survey);
            viewHolder.count_item_servey = (TextView) convertView
                    .findViewById(R.id.count_item_survey);

            viewHolder.titleInput_item_survey = (TextView) convertView
                    .findViewById(R.id.titleInput_item_survey);
            viewHolder.numberInput_item_survey = (TextView) convertView
                    .findViewById(R.id.numberInput_item_survey);
            viewHolder.countInput_item_survey = (TextView) convertView
                    .findViewById(R.id.countInput_item_survey);

            viewHolder.titleOutput_item_survey = (TextView) convertView
                    .findViewById(R.id.titleOutput_item_survey);
            viewHolder.numberOutput_item_survey = (TextView) convertView
                    .findViewById(R.id.numberOutput_item_survey);
            viewHolder.countOutput_item_survey = (TextView) convertView
                    .findViewById(R.id.countOutput_item_survey);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_item_survey.setText(list.get(position).get("title")
                .toString());
        viewHolder.sum_item_survey.setText(list.get(position).get("sum")
                .toString());
        viewHolder.count_item_servey.setText(list.get(position).get("count")
                .toString());

        viewHolder.titleInput_item_survey.setText("收入:");
        viewHolder.numberInput_item_survey.setText(list.get(position).get("numberInput")
                .toString());
        viewHolder.countInput_item_survey.setText(list.get(position).get("countInput")
                .toString());

        viewHolder.titleOutput_item_survey.setText("支出:");
        viewHolder.numberOutput_item_survey.setText(list.get(position).get("numberOutput")
                .toString());
        viewHolder.countOutput_item_survey.setText(list.get(position).get("countOutput")
                .toString());
        return convertView;
    }

    class ViewHolder {
        TextView title_item_survey;//结余标题
        TextView sum_item_survey;//结余数额
        TextView count_item_servey;//结余账期内共有几笔记录
        TextView titleInput_item_survey;//收入标题
        TextView numberInput_item_survey;//总输入数额
        TextView countInput_item_survey;//收入记录
        TextView titleOutput_item_survey;//支出标题
        TextView numberOutput_item_survey;//支出数额
        TextView countOutput_item_survey;//支出记录
    }
}