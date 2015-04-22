/**  
 * @Title: NetworkStateTool.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年2月28日 下午2:16:28  
 */ 
package mark.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/** 
 * @ClassName: NetworkStateTool 
 * @author 王帅
 * @date 2015年2月28日 下午2:16:28  
 */
public class NetworkStateTool {
	/**
	 * 判断网络连接是否可用
	 * @Title: getNetworkState 
	 * @param context
	 * @return boolean
	 */
	public static boolean isNetworkAvailable(Context context){
		if(null==context){
			return false;
		}
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();  
		if(info != null && info.isAvailable()) {
			Log.d("IMconnection", "网络连接成功");
			return true;
		} else {
			Log.d("IMconnection", "没有可用网络");			
			return false;
		}
	}
	
	/**
	 * 判断wifi连接是否可用
	 * @Title: isWifiConnected 
	 * @param context
	 * @return boolean
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断移动网络是否可用
	 * @Title: isMobileConnected
	 * @param context
	 * @return boolean
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 判断网络连接的类型  返回-1代表无网络连接
	 * @Title: getConnectedType 
	 * @param context  
	 * @return int
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

}
