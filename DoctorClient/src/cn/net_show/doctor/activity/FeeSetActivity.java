package cn.net_show.doctor.activity;

import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import org.json.JSONObject;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.utils.JsonUtils;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class FeeSetActivity extends FragmentActivity {
	private MyApplication app;
	private TextView tv_free_time,tv_fee_tv,tv_fee_call,tv_fee_add,tv_fee_private,tv_free_day,dialog_title,currentTextView;
	private Dialog inputDialog;
	private CheckBox cb_fee;
	private EditText input;
	private JsonUtils jUtils;
	@Override
	public void finish() { 
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		// TODO Auto-generated method stub
		app =(MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		super.onCreate(bundle);
		setContentView(R.layout.activity_feeset);
		findViews();
	}
	
	public void onClick(View v){
		if(v.getId()!=R.id.btn_back){
			if(inputDialog==null){
				getInputDialog();
			}
			if(!inputDialog.isShowing()){
				inputDialog.show();
			}
		}
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.llt_free_time:
			currentTextView = tv_free_time;
			dialog_title.setText("免费次数");
			input.setHint("免费次数");
			break;
		case R.id.llt_fee_call:
			currentTextView = tv_fee_call;
			dialog_title.setText("包周问诊");
			input.setHint("包周费用（元/次）");
			break;
		case R.id.llt_fee_tw:
			currentTextView = tv_fee_tv;
			dialog_title.setText("单次问诊");
			input.setHint("单次问诊费用（元/次）");
			break;
		case R.id.llt_fee_private:
			currentTextView = tv_fee_private;
			dialog_title.setText("包月问诊");
			input.setHint("包月费用（元/周）");
			break;
		case R.id.llt_fee_day:
			currentTextView = tv_free_day;
			dialog_title.setText("义诊日期");
			input.setHint("1代表周一，依次类推，7代表周日");
			input.setText("");
			break;
		case R.id.llt_fee_add:
			currentTextView = tv_fee_add;
			dialog_title.setText("门诊加号");
			input.setHint("门诊加号（元/次）");			
			break;
		default:		
			break;
		}
		if(currentTextView==null || currentTextView.getId()== R.id.txt_day_free){
			return;
		}
		if(currentTextView.getText().toString().equals("0")){
			input.setText("");
		}else{
			input.setText(currentTextView.getText());
		}
	}
	
	private void findViews(){
		tv_free_time = (TextView) findViewById(R.id.txt_free_time);
		tv_fee_tv = (TextView) findViewById(R.id.txt_text_image);
		tv_fee_call = (TextView) findViewById(R.id.txt_phone_call);
		tv_fee_add = (TextView) findViewById(R.id.txt_add);
		tv_fee_private = (TextView) findViewById(R.id.txt_private_doctor);
		tv_free_day = (TextView) findViewById(R.id.txt_day_free);
		cb_fee = (CheckBox) findViewById(R.id.cb_open);
		
		tv_free_time.setText(app.Doctor.getFreeCount()+"");
		tv_fee_tv.setText(app.Doctor.getImgTxtDiagFee()+"");
		tv_fee_call.setText(app.Doctor.getPhnDiagFee()+"");
		tv_fee_add.setText(app.Doctor.getDiagPlusFee()+"");
		tv_fee_private.setText(app.Doctor.getPrvtDiagFee()+"");
		tv_free_day.setText(getWeekday(app.Doctor.getVoltrWeekday()));
		cb_fee.setChecked(!app.Doctor.isFreeServing());
	}
	
	//"freeServing": false | true, //是否打开收费模式 false 表示打开， true 表示关闭
	//"imgTxtDiagFee":100, //图文收费
	//"phnDiagFee": 150, //电话问诊收费
	//"prvtDiagFee": 100, //私人医生收费
	//"diagPlusFee": 100, //门诊加收费
	//"voltrWeekday": 1, //义诊时间 1-7 表示周一到周日
	//"freeCount": 7 //免费问诊次数
	
	private void uploadFeeConfig(){
		JSONObject json = new JSONObject();
		String serverUrl = MyApplication.ServerUrl+"/doctor/servingfee/update?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		final int imgTextFee = Integer.parseInt(tv_fee_tv.getText().toString());
		final int phnFee = Integer.parseInt(tv_fee_call.getText().toString());
		final boolean isFee = !cb_fee.isChecked();
		final int prvtDiagFee = Integer.parseInt(tv_fee_private.getText().toString());
		final int diagPlusFee = Integer.parseInt(tv_fee_add.getText().toString());
		final int freeCount = Integer.parseInt(tv_free_time.getText().toString());
		final int voltrWeekday = getWeekday(tv_free_day.getText().toString());
		if(imgTextFee == app.Doctor.getImgTxtDiagFee() 
				&& phnFee == app.Doctor.getPhnDiagFee()
				&& isFee == app.Doctor.isFreeServing()
				&& prvtDiagFee == app.Doctor.getPrvtDiagFee()
				&& diagPlusFee == app.Doctor.getDiagPlusFee()
				&& freeCount == app.Doctor.getFreeCount()
				&& voltrWeekday == app.Doctor.getVoltrWeekday()){
			return;
		}
		
		try {
			json.put("freeServing", isFee);
			json.put("imgTxtDiagFee",imgTextFee);
			json.put("phnDiagFee", phnFee);
			json.put("prvtDiagFee", prvtDiagFee);
			json.put("diagPlusFee", diagPlusFee);			
			json.put("freeCount", freeCount);
			json.put("voltrWeekday",voltrWeekday);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpUtils.doPostAsyn(serverUrl, json.toString(),new CallBack() {
			@Override
			public void onRequestComplete(String result) {
				Log.e("资费信息修改", result);
				if(jUtils.isSuccess(result)){
					 app.Doctor.setImgTxtDiagFee(imgTextFee);
					 app.Doctor.setPhnDiagFee(phnFee);
					 app.Doctor.setFreeServing(isFee);
					 app.Doctor.setPrvtDiagFee(prvtDiagFee);
					 app.Doctor.setDiagPlusFee(diagPlusFee);
					 app.Doctor.setFreeCount(voltrWeekday);
					 app.Doctor.setVoltrWeekday(voltrWeekday);
				}
			}
		});
		
	}
	
	private void getInputDialog(){
		inputDialog = new Dialog(this,R.style.dialog);
		View v = View.inflate(this, R.layout.input_dialog, null);
		dialog_title = (TextView) v.findViewById(R.id.dialog_title);
		input = (EditText) v.findViewById(R.id.edt_input);
		Button btn = (Button) v.findViewById(R.id.dialog_btn_p);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(currentTextView!=null){
					if(!input.getText().toString().trim().equals("")){
						if(currentTextView.getId()==R.id.txt_day_free){
							int day = Integer.parseInt(input.getText().toString().trim());
							String weekday = getWeekday(day);

							currentTextView.setText(weekday);
					
						}else{
							currentTextView.setText(input.getText().toString());
						}
					}					
				}
				inputDialog.dismiss();
			}
		});
		btn = (Button) v.findViewById(R.id.dialog_btn_n);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog.dismiss();
			}
		});
		inputDialog.addContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		inputDialog.setContentView(v);
	}
	
	
	private String getWeekday(int day){
		String weekday = "星期日";
		switch(day){
		case 7:
			weekday = "星期日";
			break;
		case 1:
			weekday = "星期一";
			break;
		case 2:
			weekday = "星期二";
			break;
		case 3:
			weekday = "星期三";
			break;
		case 4:
			weekday = "星期四";
			break;
		case 5:
			weekday = "星期五";
			break;	
		case 6:
			weekday = "星期六";
			break;
		default:
			break;
			
		}
		return weekday;
	}
	
	private int getWeekday(String str){
		if(str.equals("星期一")){
			return 1;
		}else if(str.equals("星期二")){
			return 2;
		}else if(str.equals("星期三")){
			return 3;
		}else if(str.equals("星期四")){
			return 4;
		}else if(str.equals("星期五")){
			return 5;
		}else if(str.equals("星期六")){
			return 6;
		}else{
			return 7;
		}
	}

	@Override
	protected void onDestroy() {
		uploadFeeConfig();
		super.onDestroy();
	}
}
