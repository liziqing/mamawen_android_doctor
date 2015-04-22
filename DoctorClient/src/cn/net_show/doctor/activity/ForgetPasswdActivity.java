package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ForgetPasswdActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_forget_passwd);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_authcode:
			
			break;
		case R.id.btn_next:
			startActivity(new Intent(this, ResetPasswdActivity.class));
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
}
