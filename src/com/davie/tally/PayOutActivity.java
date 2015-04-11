package com.davie.tally;

import android.app.Activity;
import android.os.Bundle;
import com.davie.chart.ArcChartView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * User: davie
 * Date: 15-4-10
 */
public class PayOutActivity extends Activity {

    @ViewInject(R.id.arcCharView)
    private ArcChartView arcChartView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        ViewUtils.inject(this);
        init();
    }

    public void init(){
        Bundle bundle = getIntent().getExtras();
        int index_id = bundle.getInt("index");
        switch (index_id){
            case 1:
                String sql = " select type_id type, sum(money) money from ta_detail where dt between ? and ? ";

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