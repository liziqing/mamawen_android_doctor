/**  
 * @Title: InquiryReportItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月23日 上午12:23:31  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

/** 
 * @ClassName: InquiryReportItem 
 * @author 王帅
 * @date 2015年3月23日 上午12:23:31  
 */
public class InquiryReportItem implements Serializable{
/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	protected static final long serialVersionUID = 1L;
//  "inquiries": [{
//  "photos": [""],
//  "content": "测试问题1 妇科   Mark",
//  "createTime": 1427034865511,
//  "repportSuggestion": "哈哈哈",
//  "inquiryID": 1,
//  "description": "赵敏 8个月 女孩",
//  "reportDesc": "哈哈哈",
//  "mark": null,
//  "comment": null,
//  "drug": "该吃药了"
//}],
	public String[] photos;
	public String content;
	public long createTime;
	public String repportSuggestion;
	public String description;
	public int inquiryID;
	public String reportDesc;
	public String drug;
	public String mark;
	public String comment;
}
