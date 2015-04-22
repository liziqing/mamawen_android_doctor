/**  
 * @Title: PatientFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月16日 上午8:53:17  
 */
package cn.net_show.doctor.fragment;

import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

/**
 * @ClassName: PatientFragment
 * @Description: 患者主页面
 * @author 王帅
 * @date 2015年2月16日 上午8:53:17
 * 
 */
public class PatientFragment extends Fragment implements
		RadioGroup.OnCheckedChangeListener, View.OnClickListener {
	private View view;
	private FragmentManager fm;
	private RadioGroup radioGroup;
	private Fragment mFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_patient, container, false);
		fm = getFragmentManager();
		mFragment = new MsgRecordFragment();
		fm.beginTransaction().add(R.id.patient_container, mFragment, "MsgRecordFragment").show(mFragment).commit();
		findViews();
		return view;
	}

	private void findViews() {
		radioGroup = (RadioGroup) view.findViewById(R.id.RadioGroup_patient);
		radioGroup.setOnCheckedChangeListener(this);
		// add_contact = (ImageButton) view.findViewById(R.id.contact_add);
		// add_contact.setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentTransaction ft = fm.beginTransaction();
		switch (checkedId) {
		case R.id.msg:

			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("MsgRecordFragment");
			if (mFragment != null) {
				ft.show(mFragment).commitAllowingStateLoss();
			} else {
				mFragment = new MsgRecordFragment();
				ft.add(R.id.patient_container, mFragment, "MsgRecordFragment").show(mFragment)
						.commitAllowingStateLoss();
			}
			break;
		case R.id.contacts:
			if (mFragment != null) {
				ft.hide(mFragment);
			}
			mFragment = fm.findFragmentByTag("FriendsFragment");
			if (mFragment != null) {
				ft.show(mFragment).commitAllowingStateLoss();
			} else {
				mFragment = new FriendsFragment();
				ft.add(R.id.patient_container, mFragment, "FriendsFragment")
						.commitAllowingStateLoss();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * case R.id.contact_add: startActivity(new Intent(getActivity(),
		 * AddPatientActivity.class));
		 * getActivity().overridePendingTransition(R.anim.slide_in_right,
		 * R.anim.slide_out_left);
		 * //.overridePendingTransition(R.anim.slide_in_left
		 * ,R.anim.slide_out_right); break;
		 */
		default:
			break;
		}
	}

	public void refreshView() {
		if (mFragment != null && mFragment instanceof MsgRecordFragment) {
			((MsgRecordFragment) mFragment).refreshView();
		}
	}
	
	public void showRecord(){
		if(mFragment instanceof MsgRecordFragment){
			return;
		}
		if(fm == null){
			fm = getFragmentManager();
		}
		FragmentTransaction ft = fm.beginTransaction();
		if (mFragment != null) {
			ft.hide(mFragment); 
		}
		mFragment = fm.findFragmentByTag("MsgRecordFragment");
		if (mFragment != null) {
			ft.show(mFragment).commitAllowingStateLoss();
		} else {
			mFragment = new MsgRecordFragment();
			ft.add(R.id.patient_container, mFragment, "MsgRecordFragment").show(mFragment).commitAllowingStateLoss();
		}
	}
	
	
}
