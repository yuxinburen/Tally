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
import com.davie.utils.DateChange;
import com.davie.utils.DateUtils;
import com.davie.utils.DbUtilsHelper;
import com.davie.utils.MySQLiteOpenHelper;

import java.util.*;

/**
 * User: davie
 * Date: 15-4-10
 */
public class ListViewSurveyAdapter extends BaseAdapter {

    private static final String TAG = "ListViewSurveyAdapter";
    private Context context;
    private List<Map<String, String>> list;

    private DbUtilsHelper dbUtilsHelper;


    public ListViewSurveyAdapter(Context context) {
        super();
        if (context == null) {
            throw new IllegalArgumentException(" The context must not null ");
        }

        this.context = context;
        list = new ArrayList<Map<String, String>>();
        dbUtilsHelper = DbUtilsHelper.getInstance(context);

        //填充数据
        loadToday();//今日结余
        loadYestaday();//昨日结余
        loadThisWeek();//本周结余
        loadLastWeek();//上周结余
        loadThisMonth();//本月结余
        loadLastMonth();//上月结余
        loadThisYear();//今年结余
        loadLastYear();//去年结余
        loadAll();//全部结余
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

    /**
     * 昨日结余
     */
    public void loadYestaday(){
        int [] date = DateUtils.getCurrentDate();
        String dateStr = date[0]+"-"+date[1]+"-"+date[2];
        try {
            String day = DateChange.getLastMonth(dateStr,0,0,-1);
            int type = 2;
            queryData(type,day,day);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本周结余
     */
    public void loadThisWeek(){
        int [] date = DateUtils.getCurrentDate();
        String dateStr = date[0]+"-"+date[1]+"-"+date[2];
        try {
            String startDate =DateChange.getLastMonth(dateStr,0,0,date[3]-1);
            String endtDate = date[0]+"-"+date[1]+"-"+date[2];
            int type = 3;
            queryData(type,startDate,endtDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上周结余
     */
    public void loadLastWeek(){
        int [] date = DateUtils.getCurrentDate();
        String dateStr = date[0]+"-"+date[1]+"-"+date[2];
        try {
            String startDate = DateChange.getLastMonth(dateStr,0,0,date[3]-8);
            String endtDate = DateChange.getLastMonth(dateStr,0,0,date[3]-1);
            int type = 4;
            queryData(type,startDate,endtDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本月结余
     */
    public void loadThisMonth(){
        int [] date = DateUtils.getCurrentDate();
        String start = date[0]+"-"+date[1]+"-01";
        String end = date[0]+"-"+date[1]+"-"+date[2];
        int type = 5;
        queryData(type,start,end);
        Log.i(TAG,"start :"+start.length()+" ,end: "+end.length());
    }

    /**
     * 上月结余
     */
    public void loadLastMonth(){
        int [] date = DateUtils.getCurrentDate();
        String day = date[0]+"-"+date[1]+"-"+date[2];
        String start = date[0]+"-"+(date[1]-1)+"-01";
        String end = null;
        try {
            end = DateChange.getLastMonth(day, 0, -1, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int type = 6;
        queryData(type,start,end);
    }

    /**
     * 今年结余
     */
    public void loadThisYear(){
        int [] date = DateUtils.getCurrentDate();
        String start = date[0]+"-01-01";
        String end = date[0]+"-"+date[1]+"-"+date[2];
        int type = 7;
        queryData(type,start,end);
    }

    /**
     * 去年结余
     */
    public void loadLastYear(){
        int [] date = DateUtils.getCurrentDate();
        String start = (date[0]-1)+"-01-01";
        String end = (date[0]-1)+"-12-31";
        int type = 8;
        queryData(type,start,end);
    }

    /**
     * 全部结余
     */
    public void loadAll(){
        String start = "1900-01-01";
        String end = "2050-12-31";
        int type = 9;
        queryData(type,start,end);
    }

    //计算结余
    public void queryData(int type, String start, String end) {
        Map<String, String> map = dbUtilsHelper.queryData(start,end);
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
        list.add(map);
    }

    public void add(List<Map<String, String>> data) {
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

            viewHolder.numberInput_item_survey = (TextView) convertView
                    .findViewById(R.id.numberInput_item_survey);
            viewHolder.countInput_item_survey = (TextView) convertView
                    .findViewById(R.id.countInput_item_survey);

            viewHolder.numberOutput_item_survey = (TextView) convertView
                    .findViewById(R.id.numberOutput_item_survey);
            viewHolder.countOutput_item_survey = (TextView) convertView
                    .findViewById(R.id.countOutput_item_survey);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title_item_survey.setText(list.get(position).get("title"));
        viewHolder.sum_item_survey.setText(list.get(position).get("sum"));
        viewHolder.count_item_servey.setText(list.get(position).get("count"));

        viewHolder.numberInput_item_survey.setText(list.get(position).get("numberInput"));
        viewHolder.countInput_item_survey.setText(list.get(position).get("countInput"));

        viewHolder.numberOutput_item_survey.setText(list.get(position).get("numberOutput"));
        viewHolder.countOutput_item_survey.setText(list.get(position).get("countOutput"));
        return convertView;
    }

    class ViewHolder {
        TextView title_item_survey;//结余标题
        TextView sum_item_survey;//结余数额
        TextView count_item_servey;//结余账期内共有几笔记录
        TextView numberInput_item_survey;//总输入数额
        TextView countInput_item_survey;//收入记录
        TextView numberOutput_item_survey;//支出数额
        TextView countOutput_item_survey;//支出记录
    }
}