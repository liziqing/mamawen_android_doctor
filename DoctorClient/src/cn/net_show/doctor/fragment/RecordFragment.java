/**  
 * @Title: RecordFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @author 王帅
 * @date 2015年3月4日 上午11:05:31  
 */
package cn.net_show.doctor.fragment;

import java.util.ArrayList;

import mark.utils.HttpUtils;
import mark.utils.SimpleUtils;
import mark.widget.PullDownElasticImp;
import mark.widget.PullDownScrollView;
import mark.widget.PullDownScrollView.RefreshListener;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.AddPatientActivity;
import cn.net_show.doctor.activity.CloudActivity;
import cn.net_show.doctor.adapter.DataRecordAdapter;
//import cn.net_show.doctor.adapter.DataRecordAdapter;
import cn.net_show.doctor.model.DataRecordItem;
import cn.net_show.doctor.model.FriendsRecord;
import cn.net_show.doctor.utils.JsonUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * @ClassName: RecordFragment
 * @author 王帅
 * @date 2015年3月4日 上午11:05:31
 */
@SuppressLint("InflateParams")
public class RecordFragment extends Fragment implements RefreshListener,
		OnClickListener {
	private PullDownScrollView pullDownView;
	private View view, header;
	private ArrayList<FriendsRecord> mList;
	private ListView listView;
	private Handler mHandler;
	private JsonUtils jUtils;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_record, container, false);
		mHandler = new Handler(getActivity().getMainLooper());
		jUtils  = JsonUtils.getInstance();
		findViews();
		getFrendsRecords();
		return view;
	}

	private void findViews() {
		header = LayoutInflater.from(getActivity()).inflate(R.layout.header_adduser, null);
		Button btn = (Button) header.findViewById(R.id.btn_add_patient);
		btn.setOnClickListener(this);
		
		pullDownView = (PullDownScrollView) view.findViewById(R.id.pulldown);
		pullDownView.setPullDownElastic(new PullDownElasticImp(getActivity()));
		pullDownView.setRefreshListener(this);
		
		listView = (ListView) view.findViewById(R.id.listView);
		listView.addHeaderView(header);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), CloudActivity.class);
				intent.putExtra("data", mList.get((int)id).friend);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});
		listView.setAdapter(null);
		//test();
	}

//	private void test() {
//		mList = new ArrayList<>();
//		DataRecordItem item = null;
//		for (int i = 0; i < 10; i++) {
//			item = new DataRecordItem();
//			item.name = "第" + (i + 1) + "号妈妈";
//			item.time = SimpleUtils.randomTime("2015-01-01", "2015-03-06");
//			mList.add(item);
//		}
//		DataRecordAdapter adapter = new DataRecordAdapter(mList, getActivity());
//		listView.setAdapter(adapter);
//	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getFrendsRecords();
				pullDownView.finishRefresh("上次刷新时间   12:23");
			}

		}, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_patient:
			startActivity(new Intent(getActivity(), AddPatientActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
	
	private void getFrendsRecords(){
		if(MyApplication.Doctor==null){
			return;
		}
		String url = MyApplication.ServerUrl+"/doctor/friends/health/records?uid="+MyApplication.Doctor.getDoctorID()+"&sessionkey="+MyApplication.Doctor.getSessionKey();
		HttpUtils.doGetAsyn(url, new HttpUtils.CallBack() {
			@Override
			public void onRequestComplete(String result) {
				mList = jUtils.getFriendRecords(result);
				if(mList!=null){
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							if(getActivity()!=null){
								DataRecordAdapter adapter = new DataRecordAdapter(mList, getActivity());
								listView.setAdapter(adapter);
							}
						}
					});
				}
			}
		});
	}
	
	
}
