/**  
 * @Title: JsonUtils.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年3月13日 下午2:53:33  
 */
package cn.net_show.doctor.utils;

import java.util.ArrayList;

import mark.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.net_show.doctor.model.Doctor;
import cn.net_show.doctor.model.InquiryItem;
import cn.net_show.doctor.model.ReminderItem;

/**
 * @ClassName: JsonUtils
 * @author 王帅
 * @date 2015年3月13日 下午2:53:33
 */
public class JsonUtils {
	private static JsonUtils utils;
	private static Gson gson;
	private static final String TAG = "Response Message";

	private static Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	private JsonUtils() {

	}

	public static JsonUtils getInstance() {

		if (utils == null) {
			utils = new JsonUtils();
			getGson();
		}

		return utils;
	}

	/**
	 * 获取登陆信息
	 * 
	 * @Title: getLoginInfo
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param json
	 * @return Doctor
	 */
	public Doctor getLoginInfo(String json) {
		if (json == null || json.trim().equals("")) {
			return null;
		}
		try {
			JSONObject obj = new JSONObject(json);
			int code = obj.getInt("code");
			Logger.e(TAG, obj.getString("message"));
			if (code != 0) {
				return null;
			}
			return gson.fromJson(obj.getString("doctor"), Doctor.class);

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 解析问诊列表信息
	 * 
	 * @Title: getInquiryList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param json
	 * @return ArrayList<InquiryItem>
	 */
	public ArrayList<InquiryItem> getInquiryList(String json) {
		if (json == null || json.trim().equals("")) {
			return null;
		}
		try {
			JSONObject obj = new JSONObject(json);
			int code = obj.getInt("code");
			Logger.e(TAG, obj.getString("message"));
			if (code != 0) {
				return null;
			}
			return gson.fromJson(obj.getString("inquiries"),
					new TypeToken<ArrayList<InquiryItem>>() {
					}.getType());

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 判断返回码是否为0
	 * @param json
	 * @return
	 */
	public boolean isSuccess(String json) {
		if (json == null || json.trim().equals("")) {
			return false;
		}
		try {
			JSONObject obj = new JSONObject(json);
			int code = obj.getInt("code");
			if (code == 0) {
				return true;
			}
			Logger.e(TAG, obj.getString("message"));
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public ArrayList<ReminderItem> getReminderList(String json){
		if (json == null || json.trim().equals("")) {
			return null;
		}
		try {
			JSONObject obj = new JSONObject(json);
			int code = obj.getInt("code");
			Logger.e(TAG, obj.getString("reminders"));
			if (code != 0) {
				return null;
			}
			return gson.fromJson(obj.getString("reminders"),
						new TypeToken<ArrayList<ReminderItem>>() {
						}.getType());
		}catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
