/**  
 * @Title: EditBasicInfoActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月30日 上午12:27:18  
 */ 
package cn.net_show.doctor.activity;

import org.json.JSONException;
import org.json.JSONObject;
import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import mark.utils.Logger;
import mark.widget.Combox;
import mark.widget.MySpinner;
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
import android.widget.EditText;
import android.widget.Toast;

/** 
 * @ClassName: EditBasicInfoActivity 
 * @author 王帅
 * @date 2015年3月30日 上午12:27:18  
 */
public class EditBasicInfoActivity extends FragmentActivity {
	private MyApplication app;
	private EditText edt_name;
	private Combox cbox_job,cbox_hospital;
	private MySpinner sp_department;
	private ProgressDialog pd;
	private JsonUtils jUtils;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		jUtils = JsonUtils.getInstance();
		app =(MyApplication) getApplication();
		mHandler = new MyHandler(getMainLooper());
		setContentView(R.layout.activity_userinfo_basic);
		findViews();
	}
	private void findViews(){
		edt_name= (EditText) findViewById(R.id.edt_name);
		cbox_job =  (Combox) findViewById(R.id.cbox_job);
		cbox_hospital = (Combox) findViewById(R.id.cbox_hospital);
		sp_department =  (MySpinner) findViewById(R.id.sp_department);
		
		edt_name.setText(app.Doctor.getName());
		cbox_job.setText(app.Doctor.getTitle());
		cbox_hospital.setText(app.Doctor.getHospital());
		sp_department.setText(app.Doctor.getDepartment());
	}
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
	
	public void getProgressDialog(){
		pd= new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("请稍后……");
	}
	
	private void upload(){
		final String name = edt_name.getText().toString().trim();
		final String department = sp_department.getText().toString().trim();
		final String title = cbox_job.getText().toString().trim();
		final String hospital = cbox_hospital.getText().toString().trim();
		if("".equals(name) || "".equals(hospital) || "".equals(title)){
			Toast.makeText(this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
			return;
		}
		if(pd==null){
			getProgressDialog();
		}
		pd.show();
		String url = MyApplication.ServerUrl+"/doctor/information/update?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		JSONObject json = new JSONObject();
		try {
			json.put("name", name);
			json.put("department", department);
			json.put("title", title);
			json.put("hospital", hospital);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Logger.e("修改基本信息",json.toString());
		HttpUtils.doPostAsyn(url, json.toString(), new CallBack() {		
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				msg.what=1;
				if(jUtils.isSuccess(result)){
					msg.obj = "修改成功";
					app.Doctor.setName(name);
					app.Doctor.setTitle(title);
					app.Doctor.setHospital(hospital);
					app.Doctor.setDepartment(department);
				}else{
					msg.obj = "修改失败";					
				}
				mHandler.sendMessage(msg);
			}
		});
	}
	
	class MyHandler extends Handler{
		public MyHandler(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(pd!=null && pd.isShowing()){
					pd.dismiss();
				}
				Toast.makeText(EditBasicInfoActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				
				break;
			case 2:
				
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
}
