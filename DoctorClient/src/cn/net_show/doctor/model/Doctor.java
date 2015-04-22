/**  
 * @Title: Doctor.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月13日 下午2:15:05  
 */ 
package cn.net_show.doctor.model;

import java.io.Serializable;

import org.litepal.crud.DataSupport;

/** 
 * @ClassName: Doctor 
 * @author 王帅
 * @date 2015年3月13日 下午2:15:05  
 */
public class Doctor extends DataSupport implements Serializable{
	/**
	 *@Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	public static final long serialVersionUID = 1L;
	private int doctorID;
	private String userName;
	private String name;
	private String title;
	private String department;
	private String cellPhone;
	private String hospital;
	private String avatar;
	private int level;
	private String plateUrl;
	private String jid;
	private String imToken;
	private String sessionKey;
	private String goodAt;
	private String background;
	private String achievement;
	private boolean serveMore;
	private boolean freeServing;
	private int imgTxtDiagFee;
	private int phnDiagFee;
	private int prvtDiagFee;
	private int diagPlusFee;
	private int voltrWeekday;
	private int freeCount;
	
    /*"name": "xxx",  
    "cellPhone": "xxxx", 
    "doctorID" : 1, 
      "level": 1, 
     "title": "xxx", 
     "department": "xxx", 
      "hospital" : "xxx", 
     "plateUrl": "xxx", 
    "jid": "xxx", 
    "imToken": "xxxx" 
     “sessionKey": "xxx" 
"goodAt": "abcd",
"background": "fdaf",
"achievement": "gasdr",
"serveMore": true,
"freeServing": true,
"imgTxtDiagFee": 100,
"phnDiagFee": 150,
"prvtDiagFee": 100,
"diagPlusFee": 100,
"voltrWeekday": 1,
"freeCount": 7
*/
	
	public int getVoltrWeekday() {
		return voltrWeekday;
	}
	public int getPhnDiagFee() {
		return phnDiagFee;
	}
	public void setPhnDiagFee(int phnDiagFee) {
		this.phnDiagFee = phnDiagFee;
	}
	public int getPrvtDiagFee() {
		return prvtDiagFee;
	}
	public void setPrvtDiagFee(int prvtDiagFee) {
		this.prvtDiagFee = prvtDiagFee;
	}
	public int getDiagPlusFee() {
		return diagPlusFee;
	}
	public void setDiagPlusFee(int diagPlusFee) {
		this.diagPlusFee = diagPlusFee;
	}
	public void setImgTxtDiagFee(int imgTxtDiagFee) {
		this.imgTxtDiagFee = imgTxtDiagFee;
	}
	public String getGoodAt() {
		return goodAt;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setGoodAt(String goodat) {
		this.goodAt = goodat;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
	public String getAchievement() {
		return achievement;
	}
	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}
	
	public boolean isServeMore() {
		return serveMore;
	}
	public void setServeMore(boolean serveMore) {
		this.serveMore = serveMore;
	}
	public boolean isFreeServing() {
		return freeServing;
	}
	public void setFreeServing(boolean freeServing) {
		this.freeServing = freeServing;
	}
	public int getImgTxtDiagFee() {
		return imgTxtDiagFee;
	}
	
	public void setVoltrWeekday(int voltrWeekday) {
		this.voltrWeekday = voltrWeekday;
	}
	public int getFreeCount() {
		return freeCount;
	}
	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}
	public int getDoctorID() {
		return doctorID;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCellPhone() {
		return cellPhone;
	}
	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getPlateUrl() {
		return plateUrl;
	}
	public void setPlateUrl(String plateUrl) {
		this.plateUrl = plateUrl;
	}
	public String getJid() {
		return jid;
	}
	public void setJid(String jid) {
		this.jid = jid;
	}
	public String getImToken() {
		return imToken;
	}
	public void setImToken(String imToken) {
		this.imToken = imToken;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
		
	/*"department": "儿科",
    "cellPhone": null,
    "hospital": "北京大学人民医院",
    "achievement": "参与国家自然科学基金项目及多项院内科研及教学基金研究，在国家核心期刊及SCI杂志上发表了多篇中英文研究论著，多次荣获院、校、部级优秀教师称号，入选教育部青年英才计划。",
    "level": 3*/
}
