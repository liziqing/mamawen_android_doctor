package cn.net_show.doctor.business;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;
import org.json.JSONObject;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.impl.MyCallBack;
import cn.net_show.doctor.utils.MyRunnable;
import android.content.Context;
import android.os.Handler;

public class BehindBiz {
	private Context mContext;
	private String HOST;
	private static ExecutorService pool;
	private Handler mHandler;
	
	public BehindBiz(Context mContext) {
		super();
		
		if(mContext==null){
			throw new NullPointerException("mContext不能为null");
		}
		if(pool == null){
			pool = Executors.newFixedThreadPool(3);
		}
		this.mContext = mContext;
		HOST = mContext.getString(R.string.host);
		mHandler = new Handler(mContext.getMainLooper());
	}
	
	public void bindClient(String clientID,MyCallBack callBack){
		if(MyApplication.Doctor==null){
			return;
		}
		String serverUrl = HOST +mContext.getString(R.string.urlBindClientID)+"?uid="+MyApplication.UID+"&sessionkey="+MyApplication.SessionKey;
		JSONObject json = new JSONObject();
		try {
			json.put("clientID", clientID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pool.execute(command);
		//String result = HttpUtils.doPost(serverUrl, json.toString());
		pool.execute(MyRunnable.createPostType(mHandler,serverUrl, json.toString(), callBack));
	}
	
	public void cancel(){
		if(pool!=null){
			pool.shutdownNow();
		}
	}
}
