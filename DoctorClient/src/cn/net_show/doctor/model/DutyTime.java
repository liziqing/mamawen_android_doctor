/**  
 * @Title: DutyTime.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月20日 下午5:23:34  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

/** 
 * @ClassName: DutyTime 
 * @author 王帅
 * @date 2015年3月20日 下午5:23:34  
 */
public class DutyTime implements Serializable{
	/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	public int weekday;
	public int timeSegment;
	
	public static final int Sunday=0;
	public static final int Monday = 1;
	public static final int Tuesday = 2;
	public static final int Wednesday = 3;
	public static final int Thursday = 4;
	public static final int Friday = 5;
	public static final int Saturday = 6;	
	
	public static final int AM = 1;
	public static final int PM = 2;
	public static final int Night=3;
}
