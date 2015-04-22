/**  
 * @Title: RegistActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月19日 上午11:36:24  
 */ 
package cn.net_show.doctor.activity;

import java.util.ArrayList;
import org.jivesoftware.smack.XMPPException;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import mark.utils.HttpUtils;
import mark.utils.XmppTool;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.fragment.RegistStep1Fragment;
import cn.net_show.doctor.fragment.RegistStep2Fragment;
import cn.net_show.doctor.model.DutyTime;
import cn.net_show.doctor.utils.JsonUtils;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @ClassName: RegistActivity 
 * @author 王帅
 * @date 2015年3月19日 上午11:36:24  
 */
public class RegistActivity extends FragmentActivity{
	private FragmentManager fm;
	private Fragment fragment;
	private TextView title;
	public RegistObeject info;
	private Handler handler;
	private MyApplication app;
	private JsonUtils jUtils;
	@Override
	protected void onCreate(Bundle arg0) {
		setContentView(R.layout.activity_regist);
		info = new RegistObeject();
		handler = new MyHandler(getMainLooper());
		app = (MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		fm = getSupportFragmentManager();
		title = (TextView) findViewById(R.id.title);
		super.onCreate(arg0);		
		showPreFragment();
	}
	
	/**
	 * 显示第二页注册页面
	 * @Title: showNextFragment 
	 * @Description: TODO(这里用一句话描述这个方法的作用)    
	 * @return void
	 */
	public void showNextFragment(){
		FragmentTransaction ft = fm.beginTransaction();
		if(fragment!=null){
			ft.hide(fragment);
		}
		fragment = fm.findFragmentByTag("step2");
		if(fragment==null){
			fragment = new RegistStep2Fragment();
			ft.add(R.id.container,fragment, "step2").show(fragment).commit();
		}else{
			ft.show(fragment).commit();
		}
		title.setText("注册");
	}
	/**
	 * 显示第一页注册页面
	 * @Title: showPreFragment 
	 * @Description: TODO(这里用一句话描述这个方法的作用)    
	 * @return void
	 */
	public void showPreFragment(){
		FragmentTransaction ft = fm.beginTransaction();
		
		if(fragment!=null){
			ft.hide(fragment);
		}
		fragment = fm.findFragmentByTag("step1");
		if(fragment==null){
			fragment = new RegistStep1Fragment();
			ft.add(R.id.container,fragment, "step1").show(fragment).commit();
		}else{
			ft.show(fragment).commit();
		}
		title.setText("个人信息");
	}
	
	public class MyHandler extends Handler{
		public MyHandler(Looper looper){
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				Toast.makeText(RegistActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(RegistActivity.this, "请注意查收短信验证码！", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				MyApplication.isLogin = true;
				new Thread(){
					public void run(){
						try {
							XmppTool.login(app.Doctor.getJid(), app.Doctor.getImToken(), app);
						} catch (XMPPException e) {
							e.printStackTrace();
						}
					}
				}.start();
				startActivity(new Intent(getApplicationContext(),BodyActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
				finish();
				break;
			case 4:
				Toast.makeText(RegistActivity.this, "注册成功请登录！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(event.getKeyCode()){
		case KeyEvent.KEYCODE_BACK:	//重写返回键事件
			if(fragment instanceof RegistStep2Fragment){
				showPreFragment();
				return true;
			}
			break;
		default:
			break;
		}		
		return super.onKeyDown(keyCode, event);
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			if(fragment instanceof RegistStep2Fragment){
				showPreFragment();
				return;
			}else{
				this.finish();
			}
			break;
		}
	}


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	public static class RegistObeject{
		public String phoneNumber;
		public String password;
		public String name;
		public String hospital;
		public String department;
		public String title;
		public int deviceType = 0;
		public ArrayList<DutyTime> workTime;
	}	
	/**
	 * 注册
	 * @Title: regist 
	 * @Description: TODO(这里用一句话描述这个方法的作用)    
	 * @return void
	 */
	public void regist(){
		fragment = fm.findFragmentByTag("step1");
		((RegistStep1Fragment)fragment).getInputData();
		fragment = fm.findFragmentByTag("step2");
		((RegistStep2Fragment)fragment).getInputData();
		
		registByHttp();
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("tag", requestCode + " ---" + resultCode );
		if(resultCode == 11 && info!=null){
			info.workTime = (ArrayList<DutyTime>) data.getSerializableExtra("workTime");
			Log.e("tag", info.workTime.size()+"");
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 发送验证码
	 * @Title: getAuthCode 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param phone   
	 * @return void
	 */
	public void getAuthCode(String phone){
		String url = MyApplication.ServerUrl + "/doctor/register/validate/"+phone;
		HttpUtils.doGetAsyn(url, new HttpUtils.CallBack() {	
			@Override
			public void onRequestComplete(String result) {
				Log.e("http result",result);
				Message msg = handler.obtainMessage();
				if(jUtils.isSuccess(result)){
					msg.what = 2;
				}else{
					msg.what =1;
					msg.obj = "获取验证码失败！";
				}
				handler.sendMessage(msg);
			}
		});
	}
	
	
	public void registByHttp(){
		String url = MyApplication.ServerUrl + "/doctor/register"; 
		Gson gson = new Gson();
		Log.e("regist",gson.toJson(info));
		
		HttpUtils.doPostAsyn(url, gson.toJson(info),new HttpUtils.CallBack() {		
			@Override
			public void onRequestComplete(String result) {
								
				Log.e("http result",result);
				Message msg = handler.obtainMessage();
				
				try {
					if(result == null || result.trim().equals("")){
						msg.what = 1;
						msg.obj = "联网失败";
					}else{
						JSONObject  obj = new JSONObject(result);
						int code = obj.getInt("code");
						String message = obj.getString("message");
						if(code != 0){
							msg.what = 1;
							msg.obj = message;
						}else{
							/*Gson gson = new Gson(); 
							Doctor doc = gson.fromJson(result, Doctor.class);
							if(doc!=null && doc.getJid()!=null){
								app.Doctor = doc;
								msg.what = 3;
							}else{
								msg.what = 1;
								msg.obj = "注册失败";
							}*/
							msg.what = 4;
						}						
					}
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = 1;
					msg.obj = "注册失败";
				}			
				handler.sendMessage(msg);
			}
		});
		
	}
}
