package com.fonsview.metadata.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.time.FastDateFormat;


public class DateUtils {
	
	public enum Pattern{
		yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"), 
		yyyyMMddHHmm("yyyyMMddHHmm"), 
		yyyy_MM_dd("yyyy-MM-dd");
		
		private Pattern(String value){
			this.value = value;
		}
		
		private String value;

		public String getValue() {
			return value;
		}
		
	}
	
	/**
	 * 获取指定格式的日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date,Pattern pattern){
		return FastDateFormat.getInstance(pattern.getValue()).format(date);
	}
	
	public static String convertDate(String inputDate, String inputFormat, String outoutFormat)
			throws ParseException {
		SimpleDateFormat dateFormat1 = new SimpleDateFormat(inputDate);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(outoutFormat);
		Date date = dateFormat1.parse(inputDate);
		return dateFormat2.format(date);

	}

	public static String getCurrentTime() {
		return formatDate(new Date(),Pattern.yyyy_MM_dd_HH_mm_ss);
	}
	

	public static Set<String> getTimeListByIntervalTime(String startTime, String endTime, int intervalTime)
			throws ParseException {

		Set<String> timeSet = new TreeSet<>();
		long start = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).parse(startTime).getTime();
		long end = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).parse(endTime).getTime();

		long newTime = (1 + (start - 1) / (intervalTime * 60 * 1000)) * intervalTime * 60 * 1000;
		while (newTime < end) {
			String time = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).format(new Date(newTime));
			if (!timeSet.contains(time)) {
				timeSet.add(time);
			}
			newTime = newTime + intervalTime * 60 * 1000;
		}

		return timeSet;
	}

	/**
	 * 根据间隔小时数，获取开始时间和结束时间内所有的连续时间。
	 * 
	 * @param startTime
	 * @param endTime
	 * @param intervalTime
	 * @return
	 * @throws ParseException
	 */
	public static Set<String> getTimeListByIntervalTimeHour(String startTime, String endTime, int intervalTime)
			throws ParseException {
		return getTimeListByIntervalTime(startTime, endTime, intervalTime * 60);
	}

	/**
	 * 获取时间戳
	 * 
	 * @param pattern
	 * @param time
	 * @return
	 */
	public static long getTimeStamp(String pattern, String time) {
		long timeStamp = 0;

		SimpleDateFormat format = null;
		if (pattern == null) {
			format = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue());
		} else {
			format = new SimpleDateFormat(pattern);
		}

		try {
			timeStamp = format.parse(time).getTime();
			
			//timeStamp = format.parse(time).getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timeStamp;
	}

	/**
	 * 获取时间戳
	 * 
	 * @param time
	 * @return
	 */
	public static long getTimeStamp(String time) {
		return getTimeStamp(null, time);
	}

	/**
	 * 根据时间戳转换数据
	 * 
	 * @param pattern
	 * @param timeStamp
	 *            10位
	 * @return
	 */
	public static String getDate(String pattern, long timeStamp) {
		Calendar cal = Calendar.getInstance();
		//cal.setTimeInMillis(timeStamp*1000);
		cal.setTimeInMillis(timeStamp);

		SimpleDateFormat format = null;
		if (pattern == null) {
			format = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue());
		} else {
			format = new SimpleDateFormat(pattern);
		}

		return format.format(cal.getTime());
	}

	/**
	 * 根据时间戳转换数据
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String getDate(long timeStamp) {
		return getDate(null, timeStamp);
	}

	/**
	 * 获取时间相间intervalTime的间隔个数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param intervalTime
	 * @return
	 * @throws ParseException
	 */
	public static int getTimesByIntervalTime(String startTime, String endTime, int intervalTime) throws ParseException {
		return getTimeListByIntervalTime(startTime, endTime, intervalTime).size();
	}

	/**
	 * 获取范围内时间戳列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @param intervalTime
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, String> getTimeStampListByIntervalTime(String startTime, String endTime, int intervalTime)
			throws ParseException {
		Map<String, String> timeMap = new TreeMap<>();

		long start = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).parse(startTime).getTime();
		long end = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).parse(endTime).getTime();

		long newTime = (1 + (start - 1) / (intervalTime * 60 * 1000)) * intervalTime * 60 * 1000;
		String time = null;
		String timeStamp = null;
		while (newTime <= end) {
			time = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue()).format(new Date(newTime));
			timeStamp = new SimpleDateFormat(Pattern.yyyyMMddHHmm.getValue()).format(new Date(newTime));
			if (!timeMap.containsKey(timeStamp)) {
				timeMap.put(timeStamp, time);
			}
			newTime = newTime + intervalTime * 60 * 1000;
		}

		return timeMap;
	}

	/**
	 * 获取范围内时间戳列表
	 * 
	 * @param startTime
	 * @param endTime
	 * @param intervalTime
	 * @return
	 * @throws ParseException
	 */
	public static Set<String> getTimeStampMapByIntervalTime(String startTime, String endTime, int intervalTime)
			throws ParseException {

		SimpleDateFormat format_yyyy_MM_dd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Set<String> set = new TreeSet<>();
		long end = format_yyyy_MM_dd_HHmmss.parse(endTime).getTime();
		long long_startTime = format_yyyy_MM_dd_HHmmss.parse(getTimeStampByIntervalTime(startTime, intervalTime))
				.getTime();
		String time = null;
		while (long_startTime <= end) {
			time = format_yyyy_MM_dd_HHmmss.format(long_startTime);
			long_startTime = long_startTime + intervalTime * 60 * 1000;
			set.add(time);
		}
		return set;
	}

	public static String getTimeStampByIntervalTime(String time, int intervalTime) throws ParseException {

		SimpleDateFormat format_yyyy_MM_dd_HHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format_yyyy_MM_dd_HHmm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat format_yyyy_MM_dd_HH = new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat format_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format_mm = new SimpleDateFormat("mm");
		SimpleDateFormat format_HH = new SimpleDateFormat("HH");

		long long_time = format_yyyy_MM_dd_HHmmss.parse(time).getTime();
		long long_startTime = 0;
		switch (intervalTime) {
		case 5: {
			String mm = format_mm.format(long_time);
			String mm_0 = mm.substring(0, 1);
			int int_mm_1 = Integer.parseInt(mm.substring(1, 2));
			mm = mm_0 + (int_mm_1 % 5);
			String yyyyMMddHHmm = format_yyyy_MM_dd_HH.format(long_time) + ":" + mm;
			long_startTime = format_yyyy_MM_dd_HHmm.parse(yyyyMMddHHmm).getTime();
			break;
		}
		case 30: {
			String mm = format_mm.format(long_time);
			if (Integer.parseInt(mm) < 30) {
				mm = "00";
			} else {
				mm = "30";
			}
			String yyyyMMddHHmm = format_yyyy_MM_dd_HH.format(long_time) + ":" + mm;
			long_startTime = format_yyyy_MM_dd_HHmm.parse(yyyyMMddHHmm).getTime();
			break;
		}
		case 60: {
			long_startTime = format_yyyy_MM_dd_HH.parse(time).getTime();
			break;
		}
		case 60 * 12: {
			String hh = format_HH.format(long_time);
			if (Integer.parseInt(hh) < 12) {
				hh = "00";
			} else {
				hh = "12";
			}
			String yyyyMMddHH = format_yyyy_MM_dd.format(long_time) + ":" + hh;
			long_startTime = format_yyyy_MM_dd_HH.parse(yyyyMMddHH).getTime();
			break;
		}
		case 60 * 24: {
			long_startTime = format_yyyy_MM_dd.parse(time).getTime();
			break;
		}
		default:
		}

		return format_yyyy_MM_dd_HHmmss.format(long_startTime);

	}

	/**
	 * 相差天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int calcDayOffset(Date startTime, Date endTime) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(startTime);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endTime);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);

		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		if (year1 != year2) { // 同一年
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
					timeDistance += 366;
				} else { // 不是闰年

					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else { // 不同年
			return day2 - day1;
		}
	}

	/**
	 * 相差天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param pattern
	 *            默认格式 "yyyy-MM-dd HH:mm:ss"
	 * @return
	 * @throws ParseException
	 */
	public static int calcDayOffset(String startTime, String endTime, String pattern) throws ParseException {
		if (startTime == null || endTime == null) {
			return -1;
		}

		if (endTime.compareTo(startTime) < 0) {
			return -1;
		}

		SimpleDateFormat format = null;
		if (pattern == null) {
			format = new SimpleDateFormat(Pattern.yyyy_MM_dd_HH_mm_ss.getValue());
		} else {
			format = new SimpleDateFormat(pattern);
		}

		return calcDayOffset(format.parse(startTime), format.parse(endTime));
	}

	/**
	 * 相差天数
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public static int calcDayOffset(String startTime, String endTime) throws ParseException {
		return calcDayOffset(startTime, endTime, null);
	}

	/**
	 * 指定日期加上天数后的日期
	 * 
	 * @param num
	 *            为增加的天数
	 * @param newDate
	 *            创建时间
	 * @return
	 * @throws ParseException
	 * 
	 */
	public static String plusDay(int num, String date, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date currdate = dateFormat.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
		currdate = ca.getTime();
		String enddate = dateFormat.format(currdate);
		return enddate;
	}
	/**
	 * 统计格式转换
	 */
	public static String dateFormatSTSC(String dateStr) {
		if(StringUtils.isEmpty(dateStr)){
			return "";
		}
		DateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date;
		try {
			date = formatFrom.parse(dateStr);
		} catch (Exception e) {
			return "";
		}
		DateFormat formatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatTo.format(date);
	}
	
	/**
	 * 指定日期加上月数后的日期
	 * <p>Title: plusMonth</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2018年11月14日   
	 * @param num		增加的月数
	 * @param date		指定的日期
	 * @param format	返回日期的格式
	 * @return
	 * @throws ParseException
	 */
	public static String plusMonth(int num, String date, String format) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date currdate = dateFormat.parse(date);
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, num);// num为增加的月数，可以改变的
		currdate = ca.getTime();
		String enddate = dateFormat.format(currdate);
		return enddate;
	}
}
