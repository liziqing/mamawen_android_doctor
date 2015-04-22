package cn.net_show.doctor.activity;

import mark.utils.XmppTool;
import cn.net_show.doctor.R;
import cn.net_show.doctor.fragment.AdmissionFragment;
import cn.net_show.doctor.fragment.MarketFragment;
import cn.net_show.doctor.fragment.PatientFragment;
import cn.net_show.doctor.fragment.RecordFragment;
import cn.net_show.doctor.fragment.UserCenterFragment;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.RadioGroup;

public class BodyActivity extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener {
	private RadioGroup tab;
	private FragmentManager fm;
	private Fragment mFragment;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body);
		//如果是4.4以上版本则开启沉浸模式
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		findViews();
		initView();
	}

	private void findViews() {
		tab = (RadioGroup) findViewById(R.id.main_radio_group);
		tab.setOnCheckedChangeListener(this);
		fm = getSupportFragmentManager();

	}

	private void initView() {
		FragmentTransaction ft = fm.beginTransaction();
		mFragment = new PatientFragment();
		ft.add(R.id.main_container, mFragment, "Patient");
		ft.hide(mFragment);
		mFragment = new AdmissionFragment();
		ft.add(R.id.main_container, mFragment, "Admission")
				.show(mFragment).commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mFragment != null && mFragment instanceof PatientFragment) {
			((PatientFragment) mFragment).refreshView();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction ft = fm.beginTransaction();
		switch (checkedId) {
		case R.id.radio1:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("Admission");
			if (mFragment == null) {
				mFragment = new AdmissionFragment();
				ft.add(R.id.main_container, mFragment, "Admission")
						.show(mFragment).commitAllowingStateLoss();
			} else {
				ft.show(mFragment).commitAllowingStateLoss();
			}

			break;
		case R.id.radio2:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("Patient");
			if (mFragment == null) {
				mFragment = new PatientFragment();
				ft.add(R.id.main_container, mFragment, "Patient")
						.show(mFragment).commitAllowingStateLoss();
			} else {
				((PatientFragment) mFragment).refreshView();
				ft.show(mFragment).commitAllowingStateLoss();
			}
			break;
		case R.id.radio3:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("Market");
			if (mFragment == null) {
				mFragment = new MarketFragment();
				ft.add(R.id.main_container, mFragment, "Market")
						.show(mFragment).commitAllowingStateLoss();
			} else {
				ft.show(mFragment).commitAllowingStateLoss();
			}
			break;
		case R.id.radio4:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("UserCenter");
			if (mFragment == null) {
				mFragment = new UserCenterFragment();
				ft.add(R.id.main_container, mFragment, "UserCenter")
						.show(mFragment).commitAllowingStateLoss();
			} else {
				ft.show(mFragment).commitAllowingStateLoss();
			}
			break;
		case R.id.radio5:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("DataRecord");
			if (mFragment == null) {
				mFragment = new RecordFragment();
				ft.add(R.id.main_container, mFragment, "DataRecord")
						.show(mFragment).commitAllowingStateLoss();
			} else {
				ft.show(mFragment).commitAllowingStateLoss();
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		XmppTool.closeConnection();
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		if(intent.getBooleanExtra("isShowMsgRecord", false)){
			showMessageRecord();
		}
		super.onNewIntent(intent);
	}
	
	private void showMessageRecord(){
		if(mFragment instanceof PatientFragment){
			((PatientFragment) mFragment).showRecord();
			return;
		}
		FragmentTransaction ft = fm.beginTransaction();
		if (mFragment != null) {
			ft.hide(mFragment);
		}
		mFragment = fm.findFragmentByTag("Patient");
		if (mFragment == null) {
			mFragment = new PatientFragment();
			ft.add(R.id.main_container, mFragment, "Patient").show(mFragment).commitAllowingStateLoss();
		} else {
			((PatientFragment) mFragment).refreshView();
			ft.show(mFragment).commitAllowingStateLoss();
		}
		((PatientFragment) mFragment).showRecord();
		
	}
	
}
