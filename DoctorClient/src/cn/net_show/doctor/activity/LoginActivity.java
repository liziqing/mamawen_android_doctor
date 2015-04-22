/**  
 * @Title: LoginActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年2月28日 下午3:51:49  
 */
package cn.net_show.doctor.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;

import com.igexin.sdk.PushManager;

import mark.utils.HttpUtils;
import mark.utils.Logger;
import mark.utils.SimpleUtils;
import mark.utils.XmppTool;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.business.BehindBiz;
import cn.net_show.doctor.model.Doctor;
import cn.net_show.doctor.utils.JsonUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @ClassName: LoginActivity
 * @author 王帅
 * @date 2015年2月28日 下午3:51:49
 */
public class LoginActivity extends FragmentActivity {
	private Handler handler;
	private MyApplication app;
	private boolean isBusy = false;
	private JsonUtils jUtils;
	private EditText edt_name, edt_passwd;
	private static final String TAG = "IMconnection";
	private SharedPreferences pref;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		app = (MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		pref = getSharedPreferences(MyApplication.APPNAME, MODE_PRIVATE);
		setContentView(R.layout.activity_login);
		//如果是4.4以上版本则开启沉浸模式
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

		
		//初始化个推sdk
		PushManager.getInstance().initialize(this.getApplicationContext());
		
		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_passwd = (EditText) findViewById(R.id.edt_passwd);
		String name = pref.getString("LoginName", "");
		if(!name.equals("")){
			edt_name.setText(name);
		}
		handler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		XmppTool.closeConnection();
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn1:
			if (isBusy) {
				Log.i(TAG, "登录中……");
				return;
			}
			String name = edt_name.getText().toString();
			if (name.length() < 1) {
				Toast.makeText(this, "请输入用户名！", Toast.LENGTH_SHORT).show();
				return;
			}
			String passwd = edt_passwd.getText().toString();
			
			if (passwd.length() < 1) {
				Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
				return;
			}
			pref.edit().putString("LoginName", name).commit();
			login(name, SimpleUtils.encodePassword(passwd));
			break;
		case R.id.btn_regist:
			startActivity(new Intent(this, RegistActivity.class));
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		case R.id.btn_forget_passwd:
			startActivity(new Intent(this, ForgetPasswdActivity.class));
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		case R.id.tyyx:
			if (isBusy) {
				Log.i(TAG, "登录中……");
				return;
			}
			getDoctor();
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: login
	 * @param uid
	 * @param passwd
	 * @return void
	 */
	private void login(final String uid, final String passwd) {
		isBusy = true;
		new Thread() {
			public void run() {
				try {
					JSONObject json = new JSONObject();
					// "phoneNumber": "1300000000", //手机号
					json.put("phoneNumber", uid);
					json.put("password", passwd);
					json.put("deviceType", 0);//“deviceToken": "xxx” //苹果手机存入 devicetoken, android 可不填
					//json.put("deviceType", 0);
					Log.d("json", json.toString());
					String result = HttpUtils.doPost(MyApplication.ServerUrl
							+ "/doctor/login", json.toString());
					Logger.e("urlStr", result);
					MyApplication.Doctor = jUtils.getLoginInfo(result);
					if (MyApplication.Doctor == null) {
						isBusy = false;
						MyApplication.isLogin = false;
						toast("登陆失败");
						return;
					}
					MyApplication.Doctor.save();
					MyApplication.setLoginInfo(MyApplication.Doctor);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(LoginActivity.this, "登录成功",
									Toast.LENGTH_LONG).show();
							isBusy = false;
							MyApplication.isLogin = true;
							startActivity(new Intent(getApplicationContext(),
									BodyActivity.class));
							overridePendingTransition(R.anim.slide_in_right,
									R.anim.slide_out_left);
						}
					});
					conncetIM(MyApplication.Doctor.getJid(), MyApplication.Doctor.getImToken());
				} catch (JSONException e) {
					e.printStackTrace();
					MyApplication.isLogin = false;
				}
				isBusy = false;
			}
		}.start();
	}

	private void toast(final String msg) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
			}
		});
	}

	private void conncetIM(String JID, String passwd) {
		if(!MyApplication.clientID.equals("")){
			new BehindBiz(this).bindClient(MyApplication.clientID, null);
		}
		if (!XmppTool.getConnection(app).isConnected()) {
			toast("连接IM服务器失败");
			isBusy = false;
			return;
		}
		try {
			XmppTool.login(JID + "@"
					+ XmppTool.getConnection(app).getServiceName(), passwd, app);
			toast("登陆IM服务器成功");
		} catch (XMPPException e) {
			toast("登陆IM服务器失败");
			e.printStackTrace();
		}
	}
	
	private void getDoctor() {
		isBusy = true;
		MyApplication.Doctor = Doctor.findFirst(Doctor.class);
		String json = readDoctorJson();
		MyApplication.Doctor = jUtils.getLoginInfo(json);
		Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
		MyApplication.isLogin = true;
		startActivity(new Intent(getApplicationContext(), BodyActivity.class));
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		conncetIM(MyApplication.Doctor.getJid(), MyApplication.Doctor.getImToken());
		isBusy=false;
	}
	
	private String readDoctorJson(){
		ByteArrayOutputStream baos = null;
		try {
			InputStream is = getResources().getAssets().open("doctor.txt");
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			baos.flush();
			return baos.toString();				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	
}
