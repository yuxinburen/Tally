package com.davie.tally;

import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import com.davie.chart.ArcChartView;
import com.davie.utils.DateChange;
import com.davie.utils.DateUtils;
import com.davie.utils.DbUtilsHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

/**
 * User: davie
 * Date: 15-4-10
 */
public class PayOutActivity extends Activity {

    @ViewInject(R.id.arcCharView)
    private ArcChartView arcChartView;

    private DbUtilsHelper dbUtilsHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        ViewUtils.inject(this);
        dbUtilsHelper = DbUtilsHelper.getInstance(this);

        init();
    }

    public void init(){
        Bundle bundle = getIntent().getExtras();
        int index_id = bundle.getInt("index");
        List<DbModel> dbModelList = null;
        int [] date = DateUtils.getCurrentDate();
        String day = date[0]+"-"+date[1]+"-"+date[2];
        switch (index_id){
            case 0:
                String dt = date[0]+"-"+date[1]+"-"+date[2];
                dbModelList =  dbUtilsHelper.queryCount(dt,dt,2);
                loadData(dbModelList);
                break;
            case 1:
                String yestaday = null;
                try {
                    yestaday = DateChange.getLastMonth(day, 0, 0, -1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbModelList =  dbUtilsHelper.queryCount(yestaday,yestaday,2);
                loadData(dbModelList);
                break;
            case 2:
                String startThisWeek = null;
                String endThisWeek = null;
                try {
                    startThisWeek = DateChange.getLastMonth(day, 0, 0, date[3]-1);
                    endThisWeek = DateChange.getLastMonth(day, 0, 0, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbModelList =  dbUtilsHelper.queryCount(startThisWeek,endThisWeek,2);
                loadData(dbModelList);
                break;
            case 3:
                String startLastWeek = null;
                String endLastWeek = null;
                try {
                    startLastWeek = DateChange.getLastMonth(day, 0, 0, date[3]-8);
                    endLastWeek = DateChange.getLastMonth(day, 0, 0, date[3]-1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbModelList =  dbUtilsHelper.queryCount(startLastWeek,endLastWeek,2);
                loadData(dbModelList);
                break;
            case 4:
                String startMonth = date[0]+"-"+date[1]+"-01";
                String endMonth = date[0]+"-"+date[1]+"-"+date[2];
                dbModelList =  dbUtilsHelper.queryCount(startMonth,endMonth,2);
                loadData(dbModelList);
                break;
            case 5:
                String startLastMonth = null;
                String endLastMonth = null;
                try {
                    startLastMonth = DateChange.getLastMonth(day,0,-1,0);
                    endLastMonth = DateChange.getLastMonth(day, 0, 0, date[2]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbModelList =  dbUtilsHelper.queryCount(startLastMonth,endLastMonth,2);
                loadData(dbModelList);
                break;
            case 6:
                String startThisYear = date[0]+"-01"+"-01";
                String endThisYear = date[0]+"-"+date[1]+date[2];
                dbModelList =  dbUtilsHelper.queryCount(startThisYear,endThisYear,2);
                loadData(dbModelList);
                break;
            case 7:
                String startLastYear = (date[0]-1)+"-01-01";
                String endLastYear = date[0]+"-12-31";
                dbModelList =  dbUtilsHelper.queryCount(startLastYear,endLastYear,2);
                loadData(dbModelList);
                break;
            case 8:
                String startAll = "1900-01-01";
                String endAll = "2050-12-31";
                dbModelList =  dbUtilsHelper.queryCount(startAll,endAll,2);
                loadData(dbModelList);
                break;
        }
    }


    public void loadData(List<DbModel> dbModelList){
        float sum  = 0.0f;
        String [] cid = new String[dbModelList.size()];
        float [] money = new float[dbModelList.size()];
        float [] data = new float[dbModelList.size()];
        float [] start = new float[dbModelList.size()];
        for (int i = 0;i<dbModelList.size();i++) {
            DbModel dbModel = dbModelList.get(i);
            Map<String,String> map = dbModel.getDataMap();
            for (String key:map.keySet()){
                if("sum".equals(key)){
                    money[i] = Float.parseFloat(map.get(key));
                    sum += money[i];
                }else if("cid".equals(key)){
                    cid[i] = map.get(key);
                }
            }
        }

        for (int i = 0;i<dbModelList.size();i++){
            data[i] = money[i]/sum*360;
            Log.i("PayOutActivity"," 旋转角度: "+data[i]+" ");
            if(i == 0){
                start[i]=0;
                Log.i("PayOutActivity"," 开始角度: "+start[i]+" ");
            }else{
                start[i] = start[i-1]+data[i];
                Log.i("PayOutActivity"," 开始角度: "+start[i]+" ");
            }
        }
        float [][] set = new float[][]{start,data};
        arcChartView.setData(set);
    }
}