/**  
 * @Title: RegistStep1Fragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @author 王帅
 * @date 2015年3月20日 上午10:18:06  
 */ 
package cn.net_show.doctor.fragment;

import mark.widget.Combox;
import mark.widget.MySpinner;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.RegistActivity;
import cn.net_show.doctor.activity.WorkTimeActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/** 
 * @ClassName: RegistStep1Fragment 
 * @author 王帅
 * @date 2015年3月20日 上午10:18:06  
 */
public class RegistStep2Fragment extends Fragment implements View.OnClickListener{
	private View view,timeSelectView;
	private Button btn_regist,btn_workTime;
	private EditText edt_name;
	private Combox cbox_job,cbox_hospital;
	private MySpinner sp_department;
	private RegistActivity activity;
	
	//private PopupWindow workTimeWindow;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (RegistActivity) getActivity();
		view = inflater.inflate(R.layout.fragment_regist_step2, null);
		findViews();
		return view;
	}
	
	private void findViews(){
		btn_regist = (Button) view.findViewById(R.id.btn_regist);
		btn_workTime = (Button) view.findViewById(R.id.btn_workTime);
		btn_regist.setOnClickListener(this);
		btn_workTime.setOnClickListener(this);
		edt_name= (EditText) view.findViewById(R.id.edt_name);
		cbox_job =  (Combox) view.findViewById(R.id.cbox_job);
		cbox_hospital = (Combox) view.findViewById(R.id.cbox_hospital);
		sp_department =  (MySpinner) view.findViewById(R.id.sp_department);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_regist:
			activity.regist();				
			break;
		case R.id.btn_workTime:
			//workTimeSelect(v);
			startActivityForResult(new Intent(getActivity(), WorkTimeActivity.class),10);
			getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		default:
			break;
		}
		
	}
	
	/*private void workTimeSelect(View v){
		if(workTimeWindow==null){
			workTimeWindow = new PopupWindow(getActivity());
			workTimeWindow.setWidth(Math.max(view.getWidth()+100, 500));
			workTimeWindow.setHeight(LayoutParams.WRAP_CONTENT);
			timeSelectView = View.inflate(getActivity(), R.layout.work_time_select, null);
			workTimeWindow.setContentView(timeSelectView);
			workTimeWindow.setFocusable(true);
			
			workTimeWindow.setOutsideTouchable(true);
		}
		workTimeWindow.showAsDropDown(btn_workTime,-50,0);
	}*/
	
	public void getInputData(){
		activity.info.name = edt_name.getText().toString().trim();
		activity.info.title = cbox_job.getText().toString().trim();
		activity.info.hospital = cbox_hospital.getText().toString();
		activity.info.department = sp_department.getText().toString().trim();
	}
	
}
