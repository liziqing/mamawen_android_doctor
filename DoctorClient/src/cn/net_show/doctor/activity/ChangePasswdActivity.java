/**  
 * @Title: ChangePasswdActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月30日 上午11:48:44  
 */ 
package cn.net_show.doctor.activity;

import mark.utils.HttpUtils;
import mark.utils.Logger;
import mark.utils.SimpleUtils;
import mark.utils.HttpUtils.CallBack;
import org.json.JSONException;
import org.json.JSONObject;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.utils.JsonUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/** 
 * @ClassName: ChangePasswdActivity 
 * @author 王帅
 * @date 2015年3月30日 上午11:48:44  
 */
public class ChangePasswdActivity extends FragmentActivity {
	private ProgressDialog pd;
	private MyApplication app;
	private JsonUtils jUtils;
	private Handler mHandler;
	private EditText edt_passwd1,edt_passwd2,edt_passwdOld;
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_commit:
			upload();
			break;
		default:			
			break;
		}
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		app = (MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		mHandler = new MyHandler(getMainLooper());
		setContentView(R.layout.activity_change_passwd);
		findViews();
	}
	
	private void findViews(){
		edt_passwdOld = (EditText) findViewById(R.id.edt_old_psd);
		edt_passwd1 = (EditText) findViewById(R.id.edt_new_psd);
		edt_passwd2 = (EditText) findViewById(R.id.edt_confirm_psd);
		edt_passwdOld.requestFocus();
	}
	class MyHandler extends Handler{
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(pd!=null && pd.isShowing()){
					pd.dismiss();
				}
				Toast.makeText(ChangePasswdActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();				
				Intent intent= new Intent(app, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				break;
			case 2:
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}
	
	private void getProgressDialog(){
		pd= new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("请稍后……");
	}
	
	private void upload(){
		String oldPasswd = edt_passwdOld.getText().toString();
		String newPasswd = edt_passwd1.getText().toString();
		String confirm = edt_passwd2.getText().toString();
		
		if(oldPasswd.equals("") || newPasswd.equals("") || confirm.equals("")){
			Toast.makeText(this, "请确认填写完整", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!confirm.equals(newPasswd)){
			Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String url = MyApplication.ServerUrl+"/doctor/password/reset?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		
		JSONObject json = new JSONObject();
		try {
			json.put("id", app.Doctor.getDoctorID());
			json.put("oldPassword", SimpleUtils.encodePassword(oldPasswd));
			json.put("newPassword", SimpleUtils.encodePassword(newPasswd));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(pd==null){
			getProgressDialog();
		}
		pd.show();
		
		Logger.e("通过旧密码修改密码",json.toString());
		HttpUtils.doPostAsyn(url, json.toString(), new CallBack() {		
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				msg.what=1;
				Logger.d("通过旧密码修改密码",result);
				if(jUtils.isSuccess(result)){
					msg.obj = "修改成功";
				}else{
					msg.obj = "修改失败";
				}
				mHandler.sendMessage(msg);
			}
		});
	}
}
