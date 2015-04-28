/**  
 * @Title: RegistStep1Fragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @author 王帅
 * @date 2015年3月20日 上午10:18:06  
 */
package cn.net_show.doctor.fragment;

import mark.utils.SimpleUtils;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.RegistActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @ClassName: RegistStep1Fragment
 * @author 王帅
 * @date 2015年3月20日 上午10:18:06
 */
public class RegistStep1Fragment extends Fragment implements
		View.OnClickListener {
	private View view;
	private Button btn_next, btn_getCode;
	private String password;
	private EditText edt_cellphone, edt_passwd1, edt_passwd2, edt_authcode;
	private RegistActivity activity;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_regist_cellphone, null);
		activity = (RegistActivity) getActivity();
		findViews();
		return view;
	}

	private void findViews() {
		btn_next = (Button) view.findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
		btn_getCode = (Button) view.findViewById(R.id.btn_yzm);
		btn_getCode.setOnClickListener(this);
		edt_cellphone = (EditText) view.findViewById(R.id.edt_cellphone);
		edt_passwd1 = (EditText) view.findViewById(R.id.edt_passwd1);
		edt_passwd2 = (EditText) view.findViewById(R.id.edt_passwd2);
		edt_authcode = (EditText) view.findViewById(R.id.edt_authcode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			String authCode = edt_authcode.getText().toString().trim();
			if (authCode.length() != 6) {
				Toast.makeText(getActivity(), "请输入6位验证码！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			password = edt_passwd1.getText().toString();
			if (password.length() < 1) {
				Toast.makeText(getActivity(), "请输入密码！", Toast.LENGTH_SHORT).show();
				break;
			}
			if (password.equals(edt_passwd2.getText().toString())) {
				// password = edt_passwd1.getText().toString();
				activity.showNextFragment();
			} else {
				Toast.makeText(getActivity(), "两次输入密码不一致！", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btn_yzm:
			String phone = edt_cellphone.getText().toString().trim();
			if (SimpleUtils.isMobile(phone)) {
				activity.getAuthCode(phone);
			} else {
				Toast.makeText(getActivity(), "请输入正确手机号！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}

	public void getInputData() {
		activity.info.phoneNumber = edt_cellphone.getText().toString().trim();
		activity.info.password = SimpleUtils.encodePassword(password);
	}

}
