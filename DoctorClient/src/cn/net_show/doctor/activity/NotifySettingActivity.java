package cn.net_show.doctor.activity;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class NotifySettingActivity extends FragmentActivity implements OnCheckedChangeListener{
	private SharedPreferences preference;
	private boolean isNotifyOpen,isVoiceEnable,isVibrativeEnable,isShowDetail;
	private CheckBox cb_open,cb_voice,cb_vibrate,cb_detail;
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_notify_setting);
		init();
		findViews();
	}
	
	private void init(){
		preference = getSharedPreferences(MyApplication.APPNAME,MODE_PRIVATE);
		isNotifyOpen = preference.getBoolean("isNotifyOpen", true);
		isVoiceEnable = preference.getBoolean("isVoiceEnable", true);
		isVibrativeEnable = preference.getBoolean("isVibrativeEnable", true);
		isShowDetail = preference.getBoolean("isShowDetail", true);
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
	
	private void findViews(){
		cb_open = (CheckBox) findViewById(R.id.cb_open);
		cb_vibrate = (CheckBox) findViewById(R.id.cb_vibrative);
		cb_voice = (CheckBox) findViewById(R.id.cb_ring);
		cb_detail = (CheckBox) findViewById(R.id.cb_detial);
		cb_open.setChecked(isNotifyOpen);
		cb_vibrate.setChecked(isVibrativeEnable);
		cb_voice.setChecked(isVoiceEnable);
		cb_detail.setChecked(isShowDetail);
		cb_open.setOnCheckedChangeListener(this);
		cb_vibrate.setOnCheckedChangeListener(this);
		cb_voice.setOnCheckedChangeListener(this);
		cb_detail.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Editor editor=preference.edit();
		switch(buttonView.getId()){
		case R.id.cb_open:
			MyApplication.isNotifyOpen = isChecked;
			editor.putBoolean("isNotifyOpen", isChecked).commit();
			
			cb_detail.setEnabled(isChecked);
			cb_vibrate.setEnabled(isChecked);
			cb_voice.setEnabled(isChecked);
			break;
		case R.id.cb_ring:
			MyApplication.isNotifySound = isChecked;
			editor.putBoolean("isVoiceEnable", isChecked).commit();
			break;
		case R.id.cb_vibrative:
			MyApplication.isNotifyVibrate = isChecked;
			editor.putBoolean("isVibrativeEnable", isChecked).commit();
			break;
		case R.id.cb_detial:
			MyApplication.isNotifyShowDetail = isChecked;
			editor.putBoolean("isShowDetail", isChecked).commit();
			break;
		default:
			
			break;
		}
	}
}
