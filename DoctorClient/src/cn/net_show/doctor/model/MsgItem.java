/**  
 * @Title: MsgItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月2日 下午5:20:43  
 */
package cn.net_show.doctor.model;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 * @ClassName: MsgItem
 * @author 王帅
 * @date 2015年3月2日 下午5:20:43
 */
public class MsgItem extends DataSupport implements Serializable {
	/**
	 * @Fields serialVersionUID : 序列化
	 */
	protected static final long serialVersionUID = 1L;

	private int user1; // 用户的id
	private int user2; // 通信对象的id
	private int status; // 已读 1 未读 0
	private int isSend = 0; // 是不是JID1 发送 or 接收
	private long time; 	// 时间戳
	private int type; 	// 类型 文字 图片 语音
	private String content; // 内容 消息内容 图片地址 录音文件地址
	public int count = 0; // 给计算消息数目预留的 不写入本地数据库
	private int inquiryId=0;	//问诊id
	
	public int getInquiryId() {
		return inquiryId;
	}

	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}
	
	public int getUser1() {
		return user1;
	}

	public void setUser1(int user1) {
		this.user1 = user1;
	}

	public int getUser2() {
		return user2;
	}

	public void setUser2(int user2) {
		this.user2 = user2;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getIsSend() {
		return isSend;
	}

	public void setIsSend(int isSend) {
		this.isSend = isSend;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUnReadCount() {
		return DataSupport.where("user1 = "+user1+" and user2 = "+user2+" and status = 0 and inquiryid = "+inquiryId).count(MsgItem.class);
	}
	public static final int TYPE_STRING = 1;
	public static final int TYPE_IMAGE = 2;
	public static final int TYPE_VOICE = 3;
	public static final int TYPE_INQUIRY = 4;
	public static final int TYPE_REPORT = 5;	
	public static final int STATUS_UNREAD = 0;
	public static final int STATUS_READ = 1;
	public static final int SEND = 0;
	public static final int RECEIVE = 1;
}
