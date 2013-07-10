package org.dpi.util.locale;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * formats the dates for given locale: formatDate / formatDateTiem. or gives the format string
 * getDateFormat / getDateTimeFormat
 * @author j.kalevi
 *
 */
public class DateFormatter {

    static String date_es = "dd/MM/yyyy";
    static String fulldate_es = "EEE dd. MMM yyyy";
	static String time_es = "HH:mm";
	static String datetime_es = date_es+" "+time_es;
	static String weekdate_es = "EEE dd/MM";
	static String monthyear_es = "MMMM yyyy";
	
	private static String SQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * @param date
	 * @return
	 */
	public static String SQLDateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(SQL_DATE_FORMAT);
		return sdf.format(date);
	}
	
	/**
	 * @param date
	 * @return
	 */
	public static String HQLDateFormat(Date date) {
		return DateFormatter.SQLDateFormat(date);
	}
	
	/*
	 * returns the date in String formatted according to locale (at the moment returns same for all)
	 */
	public static String formatDate(Date date)
	{
		if(date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(date_es);
		return sdf.format(date);
	}
	
	/*
	 * returns the date and time in String formatted according to locale (at the moment returns same for all)
	 */
	public static String formatDateTime(Date date)
	{
		if(date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(datetime_es);
		return sdf.format(date);
	}	
	
	public static String formatTime(Date date)
	{
		if(date == null) return "";
		SimpleDateFormat sdf = new SimpleDateFormat(time_es);
		return sdf.format(date);		
	}
	
	public static String getDateFormat()
	{
		return date_es;
	}
	public static String getDateTimeFormat()
	{
		return datetime_es;
	}
	
	public static String getTimeFormat()
	{
		return time_es;
	}
	
	public static String weekViewDate(Date date)
	{
		String str = new SimpleDateFormat(weekdate_es).format(date);
		String first = str.substring(0, 1);
		String rest = str.substring(1);
		return first.toUpperCase()+rest;
	}
	
	public static String formatMonthAndYear(Date date)
	{
		if (date == null){
			return "";
		}
		return new SimpleDateFormat(monthyear_es).format(date);
	}
	
	public static String formatFullDate(Date date)
	{
		if (date == null){
			return "";
		}
		String str = new SimpleDateFormat(fulldate_es).format(date);
		String first = str.substring(0, 1);
		String rest = str.substring(1);
		return first.toUpperCase()+rest;		
	}
}
