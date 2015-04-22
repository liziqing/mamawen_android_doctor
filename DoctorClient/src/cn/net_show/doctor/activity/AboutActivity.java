package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class AboutActivity extends FragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		}
	}
}
