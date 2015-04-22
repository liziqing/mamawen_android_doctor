/**  
 * @Title: DateFormat.java 
 * @Package cn.net_show.doctor.utils 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月16日 下午10:40:00  
 */ 
package mark.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/** 
 * @ClassName: DateFormat 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月16日 下午10:40:00 
 *  
 */
public class DateFormat {
	
	private static DateFormat instance;
	public static final String Default_Pattern ="yyyy-MM-dd HH:mm:ss";
	/**
	  SimpleDateFormat函数语法：
	  
	  G 年代标志符
	  y 年
	  M 月
	  d 日
	  h 时 在上午或下午 (1~12)
	  H 时 在一天中 (0~23)
	  m 分
	  s 秒
	  S 毫秒
	  E 星期
	  D 一年中的第几天
	  F 一月中第几个星期几
	  w 一年中第几个星期
	  W 一月中第几个星期
	  a 上午 / 下午 标记符 
	  k 时 在一天中 (1~24)
	  K 时 在上午或下午 (0~11)
	  z 时区
	 */
	
	private DateFormat(){
		
	}
	
	public DateFormat getInstance(){
		if(instance==null){
			instance = new DateFormat();
		}
		return instance;
	}
	
	public static String toDateString(long time,String pattern){
		SimpleDateFormat sf = new SimpleDateFormat(pattern,Locale.CHINA);
		Date date = new Date(time);
		return sf.format(date);
	}
	
	public static Date strToDate(String str){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		try {
			Date date = sf.parse(str);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getFriendlyTimeString(long time){
		/*Date todayBegin = strToDate(toDateString(System.currentTimeMillis(), "yyyy-MM-dd 00:00:00"));
		Date date = new Date(time);*/
		
		//Date date = new Date(time);
		
		//Calendar today = Calendar.getInstance(Locale.CHINA);
		Calendar data_time = Calendar.getInstance(Locale.CHINA);
		data_time.setTimeInMillis(time);
		String result = "";
				
		long  now = System.currentTimeMillis();
		long interval = Math.abs(now-time)/1000;
		if(interval<5*60){
			result = "刚刚";
		}else if(interval<10*60){
			result="5分钟前";
		}else if(interval<15*60){
			result="10分钟前";
		}else{
			result = toDateString(time, "MM-dd HH:mm");
		}
		
		
		return result;
	}
}
