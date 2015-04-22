/**  
 * @Title: ReportActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月22日 下午11:17:34  
 */ 
package cn.net_show.doctor.activity;

import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.adapter.ReportAdapter;
import cn.net_show.doctor.model.InquiryReportItem;
import cn.net_show.doctor.model.ReportItem;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @ClassName: ReportActivity 
 * @author 王帅
 * @date 2015年3月22日 下午11:17:34  
 */
public class ReportActivity extends FragmentActivity {
	private MyApplication app;
	private ListView lv;
	private TextView name;
	private ArrayList<InquiryReportItem> mList;
	private MyHandler mHandler;
	private TextView emptyView;

	@Override
	protected void onCreate(Bundle bundle) {
		mList = new ArrayList<>();
		app = (MyApplication) getApplication();
		mHandler = new MyHandler(getMainLooper());
		setContentView(R.layout.activity_report);
		findViews();
		int userId = getIntent().getIntExtra("data", 0);
		
		if(userId>0){
			getReport(userId);
		}
		super.onCreate(bundle);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	private void findViews(){
		name = (TextView) findViewById(R.id.name);
		String userName = getIntent().getStringExtra("name");
		name.setText(userName);
		lv = (ListView) findViewById(R.id.lv_report);
		emptyView = (TextView) findViewById(R.id.emptyView);
		lv.setEmptyView(emptyView);
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void getReport(int userId){
		String url = MyApplication.ServerUrl + "/doctor/review/record/"+userId+"?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		HttpUtils.doGetAsyn(url, new CallBack() {	
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				if(result==null || result.equals("")){
					msg.what = 1;
					msg.obj = "网络连接失败！";
					mHandler.sendMessage(msg);
					return;
				}
				try {
					JSONObject json = new JSONObject(result);
					int code = json.getInt("code");
					msg.obj = json.getString("message");
					if(code==0){
						msg.what = 2;
						String record = json.getString("record");
						Log.i("Report",record);
						JSONObject json2 = new JSONObject(record);
						String patientReecord = json2.getString("patientReecord");
						Gson gson = new Gson();
						ArrayList<ReportItem> list = gson.fromJson(patientReecord, new TypeToken<ArrayList<ReportItem>>(){}.getType());
						if(list!=null){
							for(ReportItem item : list){
								mList.addAll(item.inquiries);
							}							
						}
						Log.i("Report",mList.size()+"==size");
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
				Toast.makeText(ReportActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(ReportActivity.this, "获取报告成功！", Toast.LENGTH_SHORT).show();
				lv.setAdapter(new ReportAdapter(mList, ReportActivity.this));
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
	
}
