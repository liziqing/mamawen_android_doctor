package cn.net_show.doctor.model.database;

import java.io.Serializable;
import java.util.List;

import org.litepal.crud.DataSupport;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.model.Role;
import cn.net_show.doctor.model.User;

public class DbUser extends DataSupport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int doctorId;		//医生ID
	private int userId;			//用户ID
	private String userName;	//用户姓名
	private String avatar;		//用户头像url
	private String cellphone;	//用户手机
	
	public String sortLetter;
	
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public static boolean isUserExsits(int userId){
		if(MyApplication.Doctor==null){
			return false;
		}
		List<DbUser> list = DataSupport.where("userId = "+userId+" and doctorId = "+MyApplication.Doctor.getDoctorID()).find(DbUser.class);
		if(list==null ||list.size()<1){
			return false;
		}else{
			return true;
		}
	}
	
	
	public Role getUserRole(){
		Role role = new Role();
		role.setAvatar(avatar);
		role.setId(userId);
		role.setName(userName);
		role.setRole(Role.ROLE_USER);
		return role;
	}
	
	public static DbUser getDbUserByUserId(int userId){
		if(MyApplication.Doctor==null){
			return null;
		}
		List<DbUser> list = DataSupport.where("userId = "+userId+" and doctorId = "+MyApplication.Doctor.getDoctorID()).find(DbUser.class);
		if(list==null ||list.size()<1){
			return null;
		}else{
			return list.get(0);
		}
	}
	
	
	public static DbUser getDbUserByRole(Role role){
		if(role == null){
			return null;
		}
		
		DbUser user = new DbUser();
		user.avatar= role.getAvatar();
		user.userName = role.getName();
		user.userId = role.getId();
		//user.cellphone = role.get;
		if(MyApplication.Doctor!=null){
			user.doctorId = MyApplication.Doctor.getDoctorID();
		}
		
		return user;
	}
	
	public static DbUser getDbUserByUser(User role){
		if(role == null){
			return null;
		}
		
		DbUser user = new DbUser();
		user.avatar= role.avator;
		user.userName = role.userName;
		user.userId = role.userID;
		user.cellphone = role.cellphone;
		if(MyApplication.Doctor!=null){
			user.doctorId = MyApplication.Doctor.getDoctorID();
		}
		
		return user;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	
	public static List<DbUser> getUsers(){
		if(MyApplication.Doctor==null){
			return null;
		}
		return DataSupport.where("doctorid = "+MyApplication.Doctor.getDoctorID()).find(DbUser.class);
	}
	@Override
	public String toString() {
		return  userName;
	}
	/**
	 * 如果没有则保存
	 * @param role
	 */
	public static void saveIfNotExist(Role role){
		DbUser user = getDbUserByRole(role);
		if(user!=null){
			if(!isUserExsits(user.userId)){
				user.save();
			}
		}
	}
	
	/**
	 * 如果没有则保存
	 * @param role
	 */
	public static void saveIfNotExist(User role){
		DbUser user = getDbUserByUser(role);
		if(user!=null){
			if(!isUserExsits(user.userId)){
				user.save();
			}
		}
	}
}
