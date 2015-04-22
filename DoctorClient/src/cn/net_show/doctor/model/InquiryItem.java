/**  
 * @Title: InquiryItem.java 
 * @Package cn.net_show.doctor.model 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月16日 上午12:44:17  
 */
package cn.net_show.doctor.model;

import java.io.Serializable;
/**
 * @ClassName: InquiryItem
 * @Description: 咨询信息实体类
 * @author 王帅
 * @date 2015年2月16日 上午12:44:17
 * 
 */
public class InquiryItem implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * {"id":1, "description":"赵敏 8个月 女孩", "content":"测试问题1 妇科   Mark",
	 * "photoes":null, "user":{"userID":2,"userName":"test2","jid":"test2"},
	 * "point":null, "drug":"该吃药了", "createTime":1426941846715,"
	 * department":"妇科", "priority":1}]}
	 */
	public int id;
	public String description;
	public String content;
	public String[] photoes;
	public User user;
	public int point;
	public String drug;
	public String department;
	public int priority;
	public String createTime;

}
