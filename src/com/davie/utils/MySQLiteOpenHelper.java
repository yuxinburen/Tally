package com.davie.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private final static String DB_NAME = "android_tally.db";
	private final static int VERSION = 1;
	public SQLiteDatabase dbConn;

	public MySQLiteOpenHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		dbConn = this.getReadableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(" create table if not exists tb_type(_id integer primary key autoincrement , type ) ");
		db.execSQL(" create table if not exists tb_category(_id integer primary key autoincrement , type_id, category ) ");
		db.execSQL(" create table if not exists tb_detail(_id integer primary key autoincrement , type_id, category_id, money, note, dt, tm ) ");
		db.execSQL(" insert into tb_type(type) values('收入') ");
		db.execSQL(" insert into tb_type(type) values('衣服') ");
		db.execSQL(" insert into tb_type(type) values('饮食') ");
		db.execSQL(" insert into tb_type(type) values('住宿') ");
		db.execSQL(" insert into tb_type(type) values('交通') ");
		db.execSQL(" insert into tb_type(type) values('生活') ");

		db.execSQL(" insert into tb_category(type_id,category) values('收入','工资') ");
		db.execSQL(" insert into tb_category(type_id,category) values('收入','外快') ");

		db.execSQL(" insert into tb_category(type_id,category) values('衣服','自己穿') ");
		db.execSQL(" insert into tb_category(type_id,category) values('衣服','礼物') ");

		db.execSQL(" insert into tb_category(type_id,category) values('饮食','三餐') ");
		db.execSQL(" insert into tb_category(type_id,category) values('饮食','请客') ");

		db.execSQL(" insert into tb_category(type_id,category) values('住宿','房租') ");
		db.execSQL(" insert into tb_category(type_id,category) values('住宿','水电') ");

		db.execSQL(" insert into tb_category(type_id,category) values('交通','公共') ");
		db.execSQL(" insert into tb_category(type_id,category) values('交通','出租') ");

		db.execSQL(" insert into tb_category(type_id,category) values('生活','娱乐') ");
		db.execSQL(" insert into tb_category(type_id,category) values('生活','书籍') ");
		db.execSQL(" insert into tb_category(type_id,category) values('生活','其他') ");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL(" drop table if exists tb_type ");
			db.execSQL(" drop table if exists tb_category ");
			db.execSQL(" drop table if exists tb_detail ");
			onCreate(db);
		}
	}

	/**
	 * @作用：执行带占位符的select语句，查询数据，返回Cursor
	 * @param sql
	 * @param selectionArgs
	 * @return Cursor
	 */
	public Cursor selectCursor(String sql, String[] selectionArgs) {
		return dbConn.rawQuery(sql, selectionArgs);
	}

	/**
	 * @作用：执行带占位符的select语句，返回结果集的个数
	 * @param sql
	 * @param selectionArgs
	 * @return int
	 */
	public int selectCount(String sql, String[] selectionArgs) {
		Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
		if (cursor != null) {
			cursor.moveToFirst();
			int count = cursor.getInt(0);
			cursor.close();
			return count;
		} else {
			return 0;
		}
	}

	/**
	 * @作用：执行带占位符的select语句，返回多条数据，放进List集合中。
	 * @param sql
	 * @param selectionArgs
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> selectList(String sql,
			String[] selectionArgs) {
		Cursor cursor = dbConn.rawQuery(sql, selectionArgs);
		return cursorToList(cursor);
	}

	/**
	 * @作用：将Cursor对象转成List集合
	 * @param cursor
	 * @return List<Map<String, Object>>集合
	 */
	public List<Map<String, Object>> cursorToList(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		String[] arrColumnName = cursor.getColumnNames();
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < arrColumnName.length; i++) {
				Object cols_value = cursor.getString(i);
				// switch (cursor.getType(i)) {
				// case 1:
				// cols_value = cursor.getInt(i);
				// break;
				// case 2:
				// cols_value = cursor.getFloat(i);
				// break;
				// case 3:
				// cols_value = cursor.getString(i);
				// break;
				// case 4:
				// cols_value = cursor.getBlob(i);
				// break;
				// default:
				// break;
				// }
				map.put(arrColumnName[i], cols_value);
			}
			list.add(map);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}

	/**
	 * @作用：执行带占位符的update、insert、delete语句，更新数据库，返回true或false
	 * @param sql
	 * @param bindArgs
	 * @return boolean
	 */
	public boolean execData(String sql, Object[] bindArgs) {
		try {
			dbConn.execSQL(sql, bindArgs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void destroy() {
		if (dbConn != null) {
			dbConn.close();
		}
	}
}
