package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class OrderListActivity extends FragmentActivity {
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_orders_list);
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
}
