package com.davie.tally;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.davie.adapter.ListViewSurveyAdapter;
import com.davie.adapter.ViewPagerAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-10
 */
public class AccountActivity extends Activity {

    @ViewInject(R.id.viewPager_account)
    private ViewPager viewPager_account;

    private ActionBar actionBar;

    private String [] tabNames;

    private List<View> viewLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ViewUtils.inject(this);

        init();
    }

    private ViewPagerAdapter viewpagerAdapter;
    public void init(){
        actionBar = getActionBar();
        loadActionBar();

        //为viewpager添加view
        viewLists = new ArrayList<View>();
        loadViewPager();
        viewpagerAdapter = new ViewPagerAdapter(viewLists);
        viewPager_account.setAdapter(viewpagerAdapter);
        viewPager_account.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    public void loadActionBar(){
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        tabNames = getResources().getStringArray(R.array.tabArray);
        for (int i = 0; i <tabNames.length ; i++) {
            Tab tab  = actionBar.newTab();
            tab.setText(tabNames[i]);
            tab.setTabListener(new ActionBar.TabListener() {
                @Override
                public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
                    viewPager_account.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
                }
                @Override
                public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
                }
            });
            actionBar.addTab(tab);
        }
    }

    public void loadViewPager(){
        //概况页面
        View survey = getLayoutInflater().inflate(R.layout.linearlayout_survey,null);
        ListView listview = (ListView) survey.findViewById(R.id.listview_survey);
        ListViewSurveyAdapter surveyAdapter = new ListViewSurveyAdapter(this);
        listview.setAdapter(surveyAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("index",position);
                intent.putExtras(bundle);
                intent.setClass(AccountActivity.this,PayOutActivity.class);
                startActivity(intent);
            }
        });

        viewLists.add(survey);
        //全部账单页面
        View all = getLayoutInflater().inflate(R.layout.linearlayout_all,null);

        viewLists.add(all);
        //month
        View month = getLayoutInflater().inflate(R.layout.linearlayout_month,null);

        viewLists.add(month);
    }
}