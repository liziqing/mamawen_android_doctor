/**  
 * @Title: AddPatientActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年2月19日 上午9:41:04  
 */
package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import cn.net_show.doctor.fragment.PhoneSMSFragment;
import cn.net_show.doctor.fragment.QRCodeFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @ClassName: AddPatientActivity
 * @Description: 添加患者
 * @author 王帅
 * @date 2015年2月19日 上午9:41:04
 * 
 */
public class AddPatientActivity extends FragmentActivity implements
		OnCheckedChangeListener {
	private FragmentManager fm;
	private CheckBox changeView;

	@Override
	protected void onCreate(Bundle saveBundle) {
		setContentView(R.layout.activity_add_patient);
		super.onCreate(saveBundle);
		fm = getSupportFragmentManager();
		findViews();
		init();
	}

	private void findViews() {
		changeView = (CheckBox) findViewById(R.id.cbox_change);
		changeView.setOnCheckedChangeListener(this);
	}

	private void init() {
		fm.beginTransaction()
				.replace(R.id.content_container, new QRCodeFragment()).commit();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_share: // 分享

			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			changeView.setText("二维码添加患者");
			fm.beginTransaction()
					.replace(R.id.content_container, new PhoneSMSFragment())
					.commit();
		} else {
			changeView.setText("手机号添加患者");
			fm.beginTransaction()
					.replace(R.id.content_container, new QRCodeFragment())
					.commit();
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
	}
}
