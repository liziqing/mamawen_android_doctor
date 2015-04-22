/**  
 * @Title: EditMoreInfoActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月30日 上午10:11:17  
 */ 
package cn.net_show.doctor.activity;

import mark.utils.HttpUtils;
import mark.utils.Logger;
import mark.utils.HttpUtils.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.utils.JsonUtils;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/** 
 * @ClassName: EditMoreInfoActivity 
 * @author 王帅
 * @date 2015年3月30日 上午10:11:17  
 */
public class EditMoreInfoActivity extends FragmentActivity {
	private MyApplication app;
	private EditText edt_goodat,edt_background,edt_achievement;
	private CheckBox cb_serverMore;
	private ProgressDialog pd;
	private JsonUtils jUtils;
	private Handler mHandler;
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
		setContentView(R.layout.activity_userinfo_more);
		findViews();
	}
	
	private void findViews(){
		edt_goodat = (EditText) findViewById(R.id.edt_goodat);
		edt_background = (EditText) findViewById(R.id.edt_selfIntroduce);
		edt_achievement = (EditText) findViewById(R.id.edt_achievement);
		cb_serverMore = (CheckBox) findViewById(R.id.cb_agree);
		edt_goodat.setText(app.Doctor.getGoodAt());
		edt_achievement.setText(app.Doctor.getAchievement());
		edt_background.setText(app.Doctor.getBackground());
		cb_serverMore.setChecked(app.Doctor.isServeMore());
	}
	
	private void upload(){
		final String goodat = edt_goodat.getText().toString().trim();
		final String background = edt_background.getText().toString().trim();
		final String achievement = edt_achievement.getText().toString().trim();
		final Boolean serverMore = cb_serverMore.isChecked();
		JSONObject json = new JSONObject();
		try {
			json.put("goodAt", goodat);
			json.put("background", background);
			json.put("achievement", achievement);
			json.put("serveMore", serverMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(pd==null){
			getProgressDialog();
		}
		pd.show();
		String url = MyApplication.ServerUrl + "/doctor/extra/information/update?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		Logger.e("修改选填信息",json.toString());
		HttpUtils.doPostAsyn(url, json.toString(), new CallBack() {		
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				msg.what=1;
				Logger.d("修改选填信息",result);
				if(jUtils.isSuccess(result)){
					msg.obj = "保存成功";
					app.Doctor.setGoodAt(goodat);
					app.Doctor.setAchievement(achievement);
					app.Doctor.setBackground(background);
					app.Doctor.setServeMore(serverMore);
				}else{
					msg.obj = "保存失败";
				}
				mHandler.sendMessage(msg);
			}
		});
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
				Toast.makeText(EditMoreInfoActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				
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
}
