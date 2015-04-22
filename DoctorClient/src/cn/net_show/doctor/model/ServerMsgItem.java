/**  
 * @Title: SendMsgItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月27日 上午10:33:05  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

/** 
 * @ClassName: SendMsgItem 
 * @author 王帅
 * @date 2015年3月27日 上午10:33:05  
 */
public class ServerMsgItem implements Serializable{
	/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1L;
//	"inquiryID": "xxx"
//		"category":1 //和请求一致
//		"subCategory": 1//和请求一致
//		“content": "xxxx" //聊天内容，如果是语音或图片则是 url
//	"description": "xxxx",
//	"suggestion": "xxx"
/*	"name": "xxxx"
	"title":"xxxx"
	"hospital": "xxxx"
	"department": "xxxxx"*/
	/**
	 * 发送者
	 */
	public Role sender;	
	/**
	 * 接收者
	 */
	public Role receiver;
	/**
	 * 关联的问诊号
	 */
	public int inquiryID;
	/**
	 * 消息种类
	 */
	public int category;
	/**
	 * 子消息种类
	 */
	public int subCategory;
	/**
	 * 消息的文字内容
	 */
	public String content;
	/**
	 * 在线诊断报告的描述内容
	 */
	public String description;
	/**
	 * 在线诊断报告的建议内容
	 */
	public String suggestion;
	
	public String name;		 	//接诊医生的姓名
	public String title;		//接诊医生的职称
	public String hospital;		//接诊医生的医院
	public String department;	//接诊医生的科室
	
	// 1 代表文字聊天，2 代表发送图片，3 代表发送语音，后两种要上传
	public static final int SUBTYPE_STRING = 1;	//文字聊天
	public static final int SUBTYPE_IMAGE = 2;	//图像
	public static final int SUBTYPE_VOICE = 3;	//声音
	public static final int TYPE_CHAT=1;	//聊天
	public static final int TYPE_INQUIRY=2;	//接诊
	public static final int TYPE_REPORT=3;	//报告
	public static final int TYPE_NEW=4; 	//新点对点问诊
}
