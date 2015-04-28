/**  
 * @Title: AdmissionFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @author 王帅
 * @date 2015年2月15日 下午8:34:16  
 */
package cn.net_show.doctor.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.litepal.crud.DataSupport;
import mark.utils.DateFormat;
import mark.utils.HttpUtils;
//import mark.utils.Logger;
import mark.widget.PullDownElasticImp;
import mark.widget.PullDownScrollView;
import mark.widget.PullDownScrollView.RefreshListener;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.AddPatientActivity;
import cn.net_show.doctor.activity.AuthActivity;
import cn.net_show.doctor.activity.CaseDetailActivity;
import cn.net_show.doctor.activity.NotifySettingActivity;
import cn.net_show.doctor.activity.ReminderActivity;
import cn.net_show.doctor.adapter.AdmissionAdapter;
import cn.net_show.doctor.model.InquiryItem;
import cn.net_show.doctor.model.ReminderItem;
import cn.net_show.doctor.utils.JsonUtils;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AdmissionFragment
 * @Description: 接诊界面
 * @author 王帅
 * @date 2015年2月15日 下午8:34:16
 * 
 */
public class AdmissionFragment extends Fragment implements RefreshListener,
		OnClickListener {
	private View view, header, reminderView;
	private PullDownScrollView pullDownView;
	private ListView listView;
	private Handler handler;
	private TextView nextReminder,reminderCount;
	// private int page = -1;
	private MyApplication app;
	private ArrayList<InquiryItem> mList;
	private JsonUtils jUtils;
	private MyCallBack callBack;
	private AdmissionAdapter adapter;
	private boolean isShow;
	private Dialog authDialog;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_admission, null);
		init();
		findViews();
		getReminderList();
		getInquiryList();

		return view;
	}

	private void getInquiryList() {
		if(app==null||MyApplication.Doctor==null){
			getActivity().finish();
			return;
		}
		if (MyApplication.Doctor.getLevel() < 1) {
			showAuthDialog();
			pullDownView.finishRefresh(DateFormat.toDateString(
					System.currentTimeMillis(), "上次刷新时间   HH:mm"));
		}
		// page++;
		String url = "/inquiry/list?uid=" + MyApplication.Doctor.getDoctorID()
				+ "&limit=10&page=0&sessionkey=" + MyApplication.Doctor.getSessionKey();// +page;
		HttpUtils.doGetAsyn(MyApplication.ServerUrl + url, callBack);

	}

	private void init() {
		app = (MyApplication) getActivity().getApplication();
		jUtils = JsonUtils.getInstance();
		handler = new MyHandler(getActivity().getMainLooper());
		callBack = new MyCallBack();
		mList = new ArrayList<>();
		adapter = new AdmissionAdapter(getActivity(), mList);
	}

	@SuppressLint("InflateParams")
	private void findViews() {
		header = LayoutInflater.from(getActivity()).inflate(R.layout.admission_menu, null);
		reminderView = header.findViewById(R.id.reminderView);
		reminderView.setOnClickListener(this);
		pullDownView = (PullDownScrollView) view.findViewById(R.id.pulldown);
		listView = (ListView) view.findViewById(R.id.listView);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		pullDownView.setRefreshListener(this);
		pullDownView.setPullDownElastic(new PullDownElasticImp(getActivity()));
		
		Button btn = (Button) view.findViewById(R.id.btn_add_patient);
		btn.setOnClickListener(this);
		btn = (Button) view.findViewById(R.id.btn_reminder);
		btn.setOnClickListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id < 0) {
					return;
				}
				if (MyApplication.Doctor.getLevel() < 1) {
					Toast.makeText(getActivity(), "认证后才能接诊哟！",Toast.LENGTH_SHORT).show();
					return;
				}
				Intent i = new Intent(getActivity(), CaseDetailActivity.class);
				i.putExtra("item", mList.get((int) id));
				startActivity(i);
				getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
	}

	@Override
	public void onResume() {
		isShow = true;
		super.onResume();
	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		getInquiryList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reminderView: // 提醒页面
			startActivity(new Intent(getActivity(), ReminderActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		case R.id.btn_add_patient:
			startActivity(new Intent(getActivity(), AddPatientActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		case R.id.btn_reminder:
			startActivity(new Intent(getActivity(), NotifySettingActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}

	public class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (!isShow) {
				return;
			}
			switch (msg.what) {
			case 1:
				Toast.makeText(getActivity(), "获取到数据失败！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 2:
				adapter = new AdmissionAdapter(getActivity(), mList);
				listView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
				// listView.setSelection(mList.size());
				break;
			case 3:
				Toast.makeText(getActivity(), "没有获取到新数据！", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
			pullDownView.finishRefresh(DateFormat.toDateString(
					System.currentTimeMillis(), "上次刷新时间   HH:mm"));
		}
	}

	private class MyCallBack implements HttpUtils.CallBack {
		@Override
		public void onRequestComplete(String result) {
			//Logger.i("获取问诊列表", result);
			ArrayList<InquiryItem> list = jUtils.getInquiryList(result);
			Message msg = handler.obtainMessage();
			if (list == null) {
				// page--;
				msg.what = 1;
				handler.sendMessage(msg);
				return;
			} else if (list.size() < 1) {
				// page--;
				msg.what = 3;
				handler.sendMessage(msg);
			} else {
				// mList.addAll(list);
				mList = list;
				msg.what = 2;
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	public void onPause() {
		isShow = false;
		super.onPause();
	}

	private void showAuthDialog() {
		if (authDialog == null) {
			authDialog = new Dialog(getActivity(), R.style.dialog);
			View v = View.inflate(getActivity(), R.layout.custom_dialog, null);

			TextView tv = (TextView) v.findViewById(R.id.dialog_title);
			tv.setText("请认证");
			tv = (TextView) v.findViewById(R.id.dialog_message);
			tv.setText("只有认证医生才能接诊！");
			Button btn = (Button) v.findViewById(R.id.dialog_btn_p);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(), AuthActivity.class));
					getActivity().overridePendingTransition(
							R.anim.slide_in_right, R.anim.slide_out_left);
					authDialog.dismiss();
				}
			});
			btn = (Button) v.findViewById(R.id.dialog_btn_n);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					authDialog.dismiss();
				}
			});
			authDialog.addContentView(v, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			authDialog.setContentView(v);
		}
		authDialog.show();
	}

	
	private void getReminderList(){
		
		if(MyApplication.Doctor==null){
			return;
		}
		List<ReminderItem> list = DataSupport.where("doctorid = "+MyApplication.Doctor.getDoctorID()).find(ReminderItem.class);
		int number = 0;
		String str_reminder = "尚未添加提醒";
		if(list!=null){
			for(ReminderItem item:list){
				if(item.getStartDate().startsWith(DateFormat.toDateString(Calendar.getInstance().getTimeInMillis(), "yyyy-MM-dd"))){	
					number++;
					if(str_reminder.equals("去添加提醒！"))
					str_reminder = item.getContent();
				}
			}
		}
		reminderCount = (TextView) header.findViewById(R.id.txt_reminder_count);
		nextReminder = (TextView) header.findViewById(R.id.txt_next_reminder);
		
		reminderCount.setText("您今天有"+number+"个提醒！");
		nextReminder.setText(str_reminder);
	}
}
