/**  
 * @Title: User.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月13日 下午4:18:17  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

/** 
 * @ClassName: User 
 * @author 王帅
 * @date 2015年3月13日 下午4:18:17  
 */
public class User implements Serializable{
	/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	public int userID;
	public String userName;
	public String avator;
	public String cellphone;
	//{"userID":2,"userName":"test2","jid":"test2"}
}
