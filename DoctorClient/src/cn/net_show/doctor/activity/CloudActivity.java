/**  
 * @Title: CloudActivity.java 
 * @Package cn.net_show.doctor.activity  
 * @author 王帅
 * @date 2015年2月20日 下午7:11:16  
 */ 
package cn.net_show.doctor.activity;

import java.util.ArrayList;

import mark.utils.HttpUtils;
import mark.utils.Logger;
import mark.widget.MoveableChart.Data;

import org.json.JSONException;
import org.json.JSONObject;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.FriendsRecord;
import cn.net_show.doctor.model.FriendsRecord.Friend;
import cn.net_show.doctor.utils.JsonUtils;
import cn.net_show.doctor.widget.CustomerChart;
import cn.net_show.doctor.widget.CustomerChart.Config;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


/** 
 * @ClassName: CloudActivity 
 * @Description: 健康云 
 * @author 王帅
 * @date 2015年2月20日 下午7:11:16 
 *  
 */
public class CloudActivity extends FragmentActivity implements View.OnClickListener{
	private FriendsRecord.Friend  friend;
	private JsonUtils jUtils;
	private Handler mHandler;
	private TextView title;
	private CustomerChart chart1,chart2,chart3,chart4;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_cloud);
		friend = (Friend) getIntent().getSerializableExtra("data");
		init();
		findViews();
		
		if(friend!=null){
			getDataList(friend.userID, "", "");
		}
	}
	
	private void findViews(){
		title = (TextView) findViewById(R.id.title);
		chart1 = (CustomerChart) findViewById(R.id.chart1);
		chart2 = (CustomerChart) findViewById(R.id.chart2);
		chart3 = (CustomerChart) findViewById(R.id.chart3);
		chart4 = (CustomerChart) findViewById(R.id.chart4);
		setChartsTitle("儿科");
	}
	
	private void init(){
		jUtils = JsonUtils.getInstance();
		mHandler = new Handler(getMainLooper());
	}

	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_contact_patient:
			startActivity(new Intent(this, IMActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_bingli:
			startActivity(new Intent(this, ReportActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
	@Override
	public void finish() {		
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	private void getDataList(int userId,String category,String subCategory){
		if(MyApplication.Doctor==null){
			return;
		}
		String url = MyApplication.ServerUrl + "/doctor/friend/"+userId+"/health/record?uid="+
				MyApplication.Doctor.getDoctorID()+"&sessionkey="+MyApplication.Doctor.getSessionKey();
		JSONObject json = new JSONObject();
		try {
			json.put("category", "宝宝");
			json.put("subCategory", "奶量");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtils.doPostAsyn(url, json.toString(), new HttpUtils.CallBack() {
			
			@Override
			public void onRequestComplete(String result) {
				final ArrayList<FriendsRecord.Record> list = jUtils.getRecordData(result);
				Logger.d(result);
				mHandler.post(new Runnable() {	
					@Override
					public void run() {
						if(list!=null && list.size()>0){
							Config config = new Config();
							config.datas = getDatas(list);
							chart4.setChartConfig(config);
							
						}
					}
				});
			}
		});
	}
	
	public ArrayList<Data> getDatas(ArrayList<FriendsRecord.Record> list){
		ArrayList<Data> datas = new ArrayList<>();
		Data item = null;
		for(FriendsRecord.Record record : list ){
			item = new Data();
			item.number = record.cycle;
			item.value = record.value;
			Logger.e("cycle="+item.number+"  value="+item.value);
			datas.add(item);
		}
		
		return datas;
		
	}
	
	
	private void setChartsTitle(String department){
		if(department.equals("儿科")){
			chart1.setTitle("体重");
			chart2.setTitle("头围");
			chart3.setTitle("身长");
			chart4.setTitle("奶量");
		}else if(department.equals("妇科")){
			chart1.setTitle("备孕体温");
			chart2.setVisibility(View.GONE);
			chart3.setVisibility(View.GONE);
			chart4.setVisibility(View.GONE);
		}else if(department.equals("产科")){
			chart1.setTitle("腹围");
			chart2.setTitle("血压");
			chart3.setTitle("体重");
			chart4.setVisibility(View.GONE);
		}
	}
}
