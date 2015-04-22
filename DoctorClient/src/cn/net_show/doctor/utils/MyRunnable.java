package cn.net_show.doctor.utils;

import mark.utils.HttpUtils;
import android.os.Handler;
import android.util.Log;
import cn.net_show.doctor.impl.MyCallBack;

public class MyRunnable implements Runnable{
	private Handler mHandler;
	private String serverUrl;
	private String params;
	private MyCallBack callBack;

	private MyRunnable(Handler mHandler,String serverUrl, String params,MyCallBack callBack) {
		super();
		this.serverUrl = serverUrl;
		this.params = params;
		this.callBack = callBack;
		this.mHandler = mHandler;
	}
	
	public static MyRunnable createPostType(Handler mHandler,String serverUrl, String params,MyCallBack callBack){
		return new MyRunnable(mHandler, serverUrl, params, callBack);
	}
	
	public static MyRunnable createGetType(Handler mHandler,String serverUrl,MyCallBack callBack){
		return new MyRunnable(mHandler, serverUrl, null, callBack);
	}
	
	@Override
	public void run() {
		final String result = isDoGet()?HttpUtils.doGet(serverUrl):HttpUtils.doPost(serverUrl, params);
		Log.e("bind client", result);
		if(callBack!=null){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					callBack.onResult(result);
				}
			});
		}		
	}
	
	private boolean isDoGet(){
		if(params==null || params.trim().equals("") || params.trim().equals("null")){
			return true;
		}
		return false;
	}
}