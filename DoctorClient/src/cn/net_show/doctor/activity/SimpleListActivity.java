package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

public class SimpleListActivity extends FragmentActivity {
	private TextView title;
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_orders_list);
		findViews();
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
		title = (TextView) findViewById(R.id.title);
		String titleS = getIntent().getStringExtra("title");
		if(titleS!=null && !titleS.trim().equals("")){
			title.setText(titleS);
		}
	}
}
