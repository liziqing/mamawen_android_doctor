/**  
 * @Title: Receiver.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月27日 上午10:38:09  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

/** 
 * @ClassName: Receiver 
 * @author 王帅
 * @date 2015年3月27日 上午10:38:09  
 */
public class Role implements Serializable{

	/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int role;
	private String name;
	private String avatar;
	
	public static final int ROLE_DOCTOR=0;
	public static final int ROLE_USER=1;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}	
}
