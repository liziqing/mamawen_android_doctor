/**  
 * @Title: ReminderItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月5日 下午1:54:51  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/** 
 * @ClassName: ReminderItem 
 * @author 王帅
 * @date 2015年3月5日 下午1:54:51  
 */
public class ReminderItem extends DataSupport implements Serializable{

	protected static final long serialVersionUID = 1L;
	
	private int reminderID;
	private String title;
	private String patientName;
	private String content;
	private String startDate;
	private String endDate;
	private String remindTime;
	private int doctorID;
	private int userID;
	private boolean remindMe;
	public int getReminderID() {
		return reminderID;
	}
	public void setReminderID(int reminderID) {
		this.reminderID = reminderID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getRemindTime() {
		return remindTime;
	}
	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}
	public int getDoctorID() {
		return doctorID;
	}
	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public boolean isRemindMe() {
		return remindMe;
	}
	public void setRemindMe(boolean remindMe) {
		this.remindMe = remindMe;
	}
	
	
//	"reminderID": 1,
//	"title": "title",
//	"patientName": "Jack",
//	"content": "content",
//	"startDate": "2015-04-01",
//	"endDate": "2015-04-21",
//	"remindTime": "10:00:00",
//	"doctorID": 1,
//	"remindMe": false,
//	"userID": 1
}
