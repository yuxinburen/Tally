package com.davie.utils;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.davie.tally.R;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import model.Category;
import model.Detail;
import model.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: davie
 * Date: 15-4-12
 */
public class DbUtilsHelper {
    private String TAG = "DbUtilsHelper";
    private DbUtils dbUtils ;
    private Context context;

    private static DbUtilsHelper dbUtilsHelper;

    public static DbUtilsHelper getInstance(Context context){
        if(dbUtilsHelper==null){
            return new DbUtilsHelper(context);
        }else {
            return dbUtilsHelper;
        }
    }
    private DbUtilsHelper(Context context){
        this.context = context;
        if (context == null) {
            throw new IllegalArgumentException(" The context must be not null ");
        }
        dbUtils = DbUtils.create(
                context,
                "tally.db",
                1,
                new DbUtils.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbUtils dbUtils, int i, int i1) {

                    }
                });
//        String [] typeArr = context.getResources().getStringArray(R.array.typeArr);
//        String [] categoryArr = context.getResources().getStringArray(R.array.categoryArr);
//        try {
//            for (int i = 0;i<typeArr.length;i++){
//                Type type = new Type();
//                type.setName(typeArr[i]);
//                dbUtils.save(type);
//            }
//            for (int i = 0;i<categoryArr.length;i++){
//                Category category = new Category();
//                category.setName(categoryArr[i]);
//                dbUtils.save(category);
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 保存一条数据到数据库
     * @param detail
     * @return
     */
    public boolean save(Detail detail){
        try {
            dbUtils.save(detail);
            return true;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询当天的明细
     * @return
     */
    public List<Detail> loadToday(){
        WhereBuilder builder = WhereBuilder.b();
        int [] date = DateUtils.getCurrentDate();
        builder.expr("dt","=", date[0]+"-"+date[1]+"-"+date[2]);
        try {
            List<Detail> all = dbUtils.findAll(
                    Selector.from(Detail.class)
                    .where(builder)
            );
            return all;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Detail> queryDetail(){
        WhereBuilder builder = WhereBuilder.b();
//        builder.and();
        return null;
    }

    public Map<String,String> queryData(String start,String end){
        WhereBuilder builder = WhereBuilder.b();
        builder.and("dt", ">=", start);
        builder.and("dt","<=",end);
        Map<String,String> map = new HashMap<String, String>();
        try {
            DbModel dbModelFirst = dbUtils.findDbModelFirst(DbModelSelector.from(Detail.class)
                    .select(" sum(money) sum, count(*) count ").where(builder));
            if (dbModelFirst != null) {
                HashMap<String,String> dataMap = dbModelFirst.getDataMap();
                for (String key:dataMap.keySet()){
                    String value = dataMap.get(key);
                    if(key.equals("count")){
                        map.put(key,"("+value+")");
                    }else
                        map.put(key,value);
                }
            }

            //收入
            WhereBuilder builder2 = WhereBuilder.b();
            builder2.and("dt", ">=", start);
            builder2.and("dt","<=",end);
            builder2.and("remark","=","1");
            DbModel dbModel = dbUtils.findDbModelFirst(DbModelSelector.from(Detail.class)
                    .select(" sum(money) numberInput, count(*) countInput ").where(builder2));
            if (dbModel != null) {
                HashMap<String,String> dataMap = dbModel.getDataMap();
                for (String key:dataMap.keySet()){
                    String value = dataMap.get(key);
                    if(key.equals("countInput")){
                        map.put(key,"("+value+")");
                    }else
                        map.put(key,value);
                }
            }
            //支出
            WhereBuilder builder1 = WhereBuilder.b();
            builder1.and("dt", ">=", start);
            builder1.and("dt","<=",end);
            builder1.and("remark","=","2");
            DbModel dbMode2 = dbUtils.findDbModelFirst(DbModelSelector.from(Detail.class)
                    .select(" sum(money) numberOutput, count(*) countOutput ").where(builder1));
            if (dbMode2 != null) {
                HashMap<String,String> dataMap = dbMode2.getDataMap();
                for (String key:dataMap.keySet()){
                    String value = dataMap.get(key);
                    if(key.equals("countOutput")){
                        map.put(key,"("+value+")");
                    }else
                        map.put(key,value);
                }
            }
            return map;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 统计饼图数据
     * @param start
     * @param end
     * @return
     */
    public List<DbModel> queryCount(String start,String end,int remark){
        WhereBuilder builder = WhereBuilder.b();
        builder.and("dt", ">=", start);
        builder.and("dt","<=",end);
        builder.and("remark","=",remark);
        Map<String,String> map = new HashMap<String, String>();
        try{
            //支出
            WhereBuilder builder1 = WhereBuilder.b();
            builder1.and("dt", ">=", start);
            builder1.and("dt","<=",end);
            builder1.and("remark","=","2");
            List<DbModel> dbModelList = dbUtils.findDbModelAll(DbModelSelector.from(Detail.class)
                    .select(" sum(money) sum, cid, count(*) count ")
                    .where(builder).groupBy("tid"));
            return dbModelList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Type getType(String name){
        WhereBuilder builder = WhereBuilder.b();
        builder.expr("name","=",name);
        try {
            Type  type = dbUtils.findFirst(Selector.from(Type.class).where(builder));
            return type;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Category getCategory(String name){
        WhereBuilder builder = WhereBuilder.b();
        builder.expr("name","=",name);
        try {
            Category  category = dbUtils.findFirst(Selector.from(Category.class).where(builder));
            return category;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Type> loadType(){
        try {
            List<Type> typeList = dbUtils.findAll(Selector.from(Type.class));
            return typeList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Category> loadCategory(){
        try {
            List<Category> categoryList = dbUtils.findAll(Selector.from(Category.class));
            return categoryList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

}
