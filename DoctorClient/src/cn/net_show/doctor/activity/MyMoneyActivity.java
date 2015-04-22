package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

public class MyMoneyActivity extends FragmentActivity {
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_my_money);
	}
	
	public void onClick(View v){
		Intent intent = null;
		switch(v.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_detail:
			intent = new Intent(this, SimpleListActivity.class);
			intent.putExtra("title", "收支明细");
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_transfer:
			Toast.makeText(this, "待完善", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
