/**  
 * @Title: WorkTimeActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月20日 下午3:55:37  
 */ 
package cn.net_show.doctor.activity;

import java.util.ArrayList;

import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;

import com.google.gson.Gson;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.DutyTime;
import cn.net_show.doctor.utils.JsonUtils;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

/** 
 * @ClassName: WorkTimeActivity 
 * @author 王帅
 * @date 2015年3月20日 下午3:55:37  
 */
public class WorkTimeActivity extends FragmentActivity {
	private int[] array = {R.id.mon_am,R.id.mon_pm,R.id.mon_nt,
			   R.id.tu_am,R.id.tu_pm,R.id.tu_nt,
			   R.id.wed_am,R.id.wed_pm,R.id.wed_nt,
			   R.id.thu_am,R.id.thu_pm,R.id.thu_nt,
			   R.id.fri_am,R.id.fri_pm,R.id.fri_nt,
			   R.id.sat_am,R.id.sat_pm,R.id.sat_nt,
			   R.id.sun_am,R.id.sun_pm,R.id.sun_nt};
	private boolean isModify;
	private MyApplication app;
	private JsonUtils jUtils;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		app =(MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		mHandler = new Handler(getMainLooper()){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 1:
					Toast.makeText(WorkTimeActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		setContentView(R.layout.activity_duty_time);
		isModify = getIntent().getBooleanExtra("isModify", false);
		super.onCreate(arg0);
	}

	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_commit:
			ArrayList<DutyTime> list = getData();
			if(isModify){
				modifyWorkTime(list);
			}else{
				Intent i = getIntent();
				i.putExtra("workTime", list);
				setResult(11, i);
				this.finish();
			}
			break;
		default:
			break;
		}
	}
	
	private ArrayList<DutyTime> getData(){
		ArrayList<DutyTime> list = new ArrayList<>();
		DutyTime workTime = null;
		CheckBox cb = null;
		for(int i=0;i<array.length;i++){
			cb = (CheckBox) findViewById(array[i]);
			if(cb.isChecked()){
				workTime = getDutyTime(i);
				list.add(workTime);
			}
		}
		return list;
	}
	
	private DutyTime getDutyTime(int position){
		int day = position/3 +1;
		if(day == 7 ){
			day = 0;
		}
		int time = position%3 +1;
		DutyTime workTime = new DutyTime();
		workTime.weekday = day;
		workTime.timeSegment = time;
		return workTime;
	}
	
	@Override
	public void finish() {
		//overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	private void modifyWorkTime(ArrayList<DutyTime> list){
		if(list==null){//||list.size()<1
			return;
		}
		String serverUrl = MyApplication.ServerUrl+"/doctor/worktime/update?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		Gson gson = new Gson();
		HttpUtils.doPostAsyn(serverUrl, gson.toJson(list), new CallBack() {	
			@Override
			public void onRequestComplete(String result) {
				Message msg = mHandler.obtainMessage();
				msg.what=1;
				if(jUtils.isSuccess(result)){
					msg.obj="保存成功！";
				}else{
					msg.obj="保存失败！";
				}
				mHandler.sendMessage(msg);
			}
		});
		
	
	}
}
