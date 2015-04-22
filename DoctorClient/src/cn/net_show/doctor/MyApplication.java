package cn.net_show.doctor;

import java.util.HashMap;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import mark.utils.CrashHandler;
import cn.net_show.doctor.broadcast.NetworkStateBroadCast;
import cn.net_show.doctor.model.Doctor;
import cn.net_show.doctor.utils.SoundPoolUtil;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;

/**
 * @ClassName: MyApplication
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月15日 上午11:26:48
 * 
 */
public class MyApplication extends LitePalApplication {
	public static boolean isLogin = false;
	public static final String MARK = "iphone";
	public static Doctor Doctor;
	private static HashMap<String, Bitmap> imageCache;
	public static final String ServerUrl = "http://115.159.49.31:9000";
	public static final String TAG = "DoctorClient";
	public static final String APPNAME = "DoctorClient";
	public static String SessionKey = "";
	public static int UID = 0;
	public static String clientID="";
	public static boolean isNotifyOpen = true;
	public static boolean isNotifySound = true;
	public static boolean isNotifyVibrate = true;
	public static boolean isNotifyShowDetail = true;
	
	@Override
	public void onCreate() {
		// Ioc.getIoc().init(this);//必须在super.onCreate()之前调用
		super.onCreate();
		init();
		SQLiteDatabase db = Connector.getDatabase();
		initImageLoader(this);
	}

	public static HashMap<String, Bitmap> getImageCache(){
		if(imageCache == null){
			return new HashMap<>();
		}
		return imageCache;
	}
	
	private void init() {
		// 注册全局异常捕获 crashHandler
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		SoundPoolUtil.init(this);
		registerReceiver();
		getConfigration();
		// 用于组件的倒计时
		//new MyCountDownTimer(Integer.MAX_VALUE, 1000).start();
	}

	private void registerReceiver() {
		NetworkStateBroadCast receiver = new NetworkStateBroadCast();
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, filter);
	}

	/**
	 * 初始化imageLoader默认配置
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		//config.writeDebugLogs(); // Remove for release app
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
	}
	
	public static Doctor getLoginedDoctor(){
		if(Doctor==null){
			Doctor = DataSupport.findLast(Doctor.class);
		}
		return Doctor;
	}
	
	private void getConfigration(){
		SharedPreferences sp = getSharedPreferences(APPNAME, MODE_PRIVATE);
		isNotifyOpen = sp.getBoolean("isNotifyOpen", true);
		isNotifySound = sp.getBoolean("isVoiceEnable", true);
		isNotifyVibrate = sp.getBoolean("isVibrativeEnable", true);
		isNotifyShowDetail = sp.getBoolean("isShowDetail", true);
		clientID = sp.getString("clientID", "");
	}
	
	public static void setLoginInfo(Doctor doctor){
		if(doctor==null){
			return;
		}
		SessionKey = doctor.getSessionKey();
		UID = doctor.getDoctorID();
	}
	
	public static void clean(){
		SessionKey = "";
		UID = 0;
		isLogin = false;
		Doctor = null;
	}
}
