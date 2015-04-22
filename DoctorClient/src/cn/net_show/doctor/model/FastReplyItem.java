/**  
 * @Title: FastReplyItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月22日 下午1:32:02  
 */
package cn.net_show.doctor.model;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/**
 * @ClassName: FastReplyItem
 * @author 王帅
 * @date 2015年3月22日 下午1:32:02
 */
public class FastReplyItem extends DataSupport implements Serializable {
	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	protected static final long serialVersionUID = 1L;

	public static final int TYPE_STRING = 0;
	public static final int TYPE_VOICE = 1;

	private String owner;
	private int type;
	private String content;
	private String subject;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

}
