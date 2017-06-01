package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间的操作
 * 
 * @version 1.0
 * 
 */

public class DateTimeUtils {
	
	private static Logger logger = LogManager.getLogger();

	/**
	 * 指定文件名、截图的日期格式
	 * 
	 * @return 返回时间格式，如：2016-07-22 15.15.30.542
	 * 
	 */
	public static String getFileDateTime() {
		return formatedTime("yyyy_MM_dd_HH_mm_ss");
	}

	/**
	 * 将表示时间的长整数转化为日期格式
	 * 
	 * @param millis 代表时间的长整数
	 * 
	 * @return 时间
	 * 
	 */
	public static Date getCurrentTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime(); 
	}

	/**
	 * 根据自定义格式获取系统当前时间
	 * 
	 * @param format
	 *            时间格式 eg: yyyy-MM-dd HH:mm:ss:SSS
	 * @return 根据自定义格式返回系统当前时间
	 * 
	 */
	public static String formatedTime(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}

	/**
	 * 根据自定义格式指定时间
	 * 
	 * @param format
	 *            时间格式 eg: yyyy-MM-dd HH:mm:ss:SSS
	 * @param datetime
	 *            eg: 2015-02-12 14:00:00
	 * @return 指定格式的日期时间
	 * 
	 */
	public static String formatedTime(String format, String datetime) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.format(sdf.parse(datetime));
		} catch (ParseException e) {
			logger.error("DateTimeUtil.formatedTime() error!", e);
		}
		return null;
	}

}