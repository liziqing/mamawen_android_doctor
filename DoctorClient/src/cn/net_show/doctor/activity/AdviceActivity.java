/**  
 * @Title: AdviceActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月22日 下午7:01:38  
 */ 
package cn.net_show.doctor.activity;

import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import org.json.JSONException;
import org.json.JSONObject;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.database.DbInquiry;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/** 
 * @ClassName: AdviceActivity 
 * @author 王帅
 * @date 2015年3月22日 下午7:01:38  
 */
public class AdviceActivity extends FragmentActivity{
	private MyApplication app;
	private EditText edt_issue,edt_advice;
	private MyHandler mHandler;
	private DbInquiry dbInquiry;
	private boolean isBusy = false;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		mHandler = new MyHandler(getMainLooper());
		app = (MyApplication) getApplication();
		dbInquiry = (DbInquiry) getIntent().getSerializableExtra("data");
		setContentView(R.layout.activity_advice);
		findViews();
		
	}

	private void findViews(){
		edt_issue = (EditText) findViewById(R.id.edt_issue);
		edt_advice = (EditText) findViewById(R.id.edt_advice);
	}
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	public void onClick(View v){
		if(isBusy){
			Toast.makeText(AdviceActivity.this, "联网提交报告中！", Toast.LENGTH_SHORT).show();
			return;
		}
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_submit:
			if(dbInquiry==null){
				Toast.makeText(AdviceActivity.this, "错误的问诊Id！", Toast.LENGTH_SHORT).show();
				return;
			}
			String description = edt_issue.getText().toString().trim();
			String suggestion = edt_advice.getText().toString().trim();
			if(edt_advice.length()<1 || edt_issue.length()<1){
				Toast.makeText(AdviceActivity.this, "请将报告填写完整！", Toast.LENGTH_SHORT).show();
				break;
			}
			submit(dbInquiry.getInquiryId(), description, suggestion);
			
			break;
		default:
			break;
		}
	}
	
	class MyHandler extends Handler{

		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				Toast.makeText(AdviceActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(AdviceActivity.this, "提交报告成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	//"inquiryID": 12,
	//"description"： ”xxxx",
	//"suggestion": "xxx"
	private void submit(int inquiryID,String description,String suggestion){
		isBusy = true;
		String url = MyApplication.ServerUrl+"/doctor/inquiry/report?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		JSONObject obj = new JSONObject();
		try {
			obj.put("inquiryID", inquiryID);
			obj.put("description", description);
			obj.put("suggestion", suggestion);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		HttpUtils.doPostAsyn(url, obj.toString(),new CallBack() {	
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				if(result==null || result.equals("")){
					msg.what = 1;
					msg.obj = "网络连接失败！";
					mHandler.sendMessage(msg);
					isBusy = false;
					return;
				}else{
					try {
						JSONObject json = new JSONObject(result);
						int code = json.getInt("code");
						msg.obj = json.getString("message");
						if(code==0){
							msg.what = 2;
						}else{
							msg.what = 1;
						}
						mHandler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
						msg.what = 1;
						msg.obj = e.getMessage();
						mHandler.sendMessage(msg);
					}
					isBusy = false;
				}
			}
		});
	}
}
