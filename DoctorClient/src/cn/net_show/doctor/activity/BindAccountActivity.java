package cn.net_show.doctor.activity;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class BindAccountActivity extends FragmentActivity {
	private MyApplication app;
	private TextView tv_doctorId,tv_phone;
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		app=(MyApplication) getApplication();
		super.onCreate(bundle);
		setContentView(R.layout.activity_bind_account);
		findViews();
	}
	
	private void findViews(){
		tv_doctorId = (TextView) findViewById(R.id.txt_doctorId);
		tv_phone = (TextView) findViewById(R.id.txt_phone);
		tv_doctorId.setText(app.Doctor.getDoctorID()+"");
		tv_phone.setText(app.Doctor.getCellPhone());
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		}
	}
}
