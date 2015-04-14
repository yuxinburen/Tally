package com.davie.utils;

import java.util.Calendar;

public class DateChange {
	/**
	 * ��ȡָ���µ�ǰһ�£��꣩���һ�£��꣩
	 * 
	 * @param dateStr
	 * @param addYear
	 * @param addMonth
	 * @param addDate
	 * @return �����ʱ�ڸ�ʽΪyyyy-MM����������ڸ�ʽΪyyyy-MM
	 * @throws Exception
	 */
	public static String getLastMonth(String dateStr, int addYear,
			int addMonth, int addDate) throws Exception {
		try {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date sourceDate = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sourceDate);
			cal.add(Calendar.YEAR, addYear);
			cal.add(Calendar.MONTH, addMonth);
			cal.add(Calendar.DATE, addDate);

			java.text.SimpleDateFormat returnSdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			String dateTmp = returnSdf.format(cal.getTime());
			java.util.Date returnDate = returnSdf.parse(dateTmp);
			return dateTmp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * ��ȡָ���µ�ǰһ�£��꣩���һ�£��꣩
	 * 
	 * @param dateStr
	 * @param addYear
	 * @param addMonth
	 * @param addDate
	 * @return �����ʱ�ڸ�ʽΪyyyy-MM-dd����������ڸ�ʽΪyyyy-MM-dd
	 * @throws Exception
	 */
	public static String getLastDay(String dateStr, int addYear, int addMonth,
			int addDate) throws Exception {
		try {
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			java.util.Date sourceDate = sdf.parse(dateStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(sourceDate);
			cal.add(Calendar.YEAR, addYear);
			cal.add(Calendar.MONTH, addMonth);
			cal.add(Calendar.DATE, addDate);

			java.text.SimpleDateFormat returnSdf = new java.text.SimpleDateFormat(
					"yyyy-MM-dd");
			String dateTmp = returnSdf.format(cal.getTime());
			java.util.Date returnDate = returnSdf.parse(dateTmp);
			return dateTmp;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * ����
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(DateChange.getLastMonth("2011-06-01", 0, 0, -7));// 2011-05
			System.out.println(DateChange.getLastMonth("2011-06-01", 0, -6, -1));// 2010-12
			System.out.println(DateChange.getLastMonth("2011-06-01", -1, 0, -1));// 2010-06
		} catch (Exception e) {
		}

	}
}
