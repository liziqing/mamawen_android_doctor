package cn.net_show.doctor.model.database;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

import cn.net_show.doctor.model.InquiryItem;
import cn.net_show.doctor.model.User;


public class DbInquiry extends DataSupport implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int inquiryId;
	private String description;
	private String content;
	private String photoes="";
	private int point;
	private String drug;
	private String department;
	private int priority;
	private String createTime;
	private int userID;
	private String userName;
	
	public int getInquiryId() {
		return inquiryId;
	}
	public void setInquiryId(int inquiryId) {
		this.inquiryId = inquiryId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhotoes() {
		return photoes;
	}
	public void setPhotoes(String photoes) {
		this.photoes = photoes;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getDrug() {
		return drug;
	}
	public void setDrug(String drug) {
		this.drug = drug;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public static DbInquiry getDbModel(InquiryItem item){
		if(item==null){
			return null;
		}
		
		DbInquiry dbInquiry = new DbInquiry();
		dbInquiry.inquiryId = item.id;
		dbInquiry.content = item.content;
		dbInquiry.createTime = item.createTime;
		dbInquiry.department = item.department;
		dbInquiry.description = item.description;
		dbInquiry.drug = item.drug;
		dbInquiry.photoes = "";
		if(item.photoes!=null){
			for(String str : item.photoes){
				dbInquiry.photoes = dbInquiry.photoes+str+"$";
			}
			if(dbInquiry.photoes.endsWith("$")){
				dbInquiry.photoes.substring(0, dbInquiry.photoes.length()-1);
			}
		}
		dbInquiry.point = item.point;
		dbInquiry.priority = item.priority;
		if(item.user!=null){
			dbInquiry.userID=item.user.userID;
			dbInquiry.userName = item.user.userName;
		}
		return dbInquiry;
	}
	
	public InquiryItem toInquiryItem(){
		InquiryItem item= new InquiryItem();
		item.content = content;
		item.createTime =createTime;
		item.department = department;
		item.description = description;
		item.drug = drug;
		item.id = inquiryId;
		item.photoes = photoes.split("\\$");
		item.point = point;
		item.priority = priority;
		item.user = new User();
		item.user.userID=userID;
		item.user.userName=userName;
		return item;
	}
}
