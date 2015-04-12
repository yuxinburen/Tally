package com.davie.tally;

import android.app.Activity;
import android.os.Bundle;
import com.davie.chart.ArcChartView;
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

        switch (index_id){
            case 1:
                String [] cid = new String[5];
                float [] data = new float[5];
                int [] date = DateUtils.getCurrentDate();
                String dt = date[0]+"-"+date[1]+"-"+date[2];
                List<DbModel> dbModelList =  dbUtilsHelper.queryCount(dt,dt,2);
                for (int i = 0;i<dbModelList.size();i++) {
                    DbModel dbModel = dbModelList.get(i);
                    Map<String,String> map = dbModel.getDataMap();
                    for (String key:map.keySet()){
                        if("sum".equals(key)){
                            data[i] = Float.parseFloat(map.get(key));
                        }else if("cid".equals(key)){
                            cid[i] = map.get(key);
                        }
                    }
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
        }
    }
}