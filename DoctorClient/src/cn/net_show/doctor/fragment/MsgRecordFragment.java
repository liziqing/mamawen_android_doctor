/**  
 * @Title: MsgRecordFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月16日 下午5:46:53  
 */ 
package cn.net_show.doctor.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.litepal.crud.DataSupport;

import mark.utils.DateFormat;
import mark.widget.PullDownElasticImp;
import mark.widget.PullDownScrollView;
import mark.widget.PullDownScrollView.RefreshListener;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.IMActivity;
import cn.net_show.doctor.adapter.MsgRecordAdapter;
import cn.net_show.doctor.model.MsgItem;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/** 
 * @ClassName: MsgRecordFragment 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月16日 下午5:46:53 
 */
public class MsgRecordFragment extends Fragment implements RefreshListener{
	private View view;//,header;
	private PullDownScrollView pullDownView;
	private ListView listView;
	private MyApplication app;
	private List<MsgItem> list;// = new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		app = (MyApplication) getActivity().getApplication();
		view = inflater.inflate(R.layout.pulldown_listview, container, false);
		list = new ArrayList<>();
		findViews();
		return view;
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private void findViews(){
		pullDownView = (PullDownScrollView) view.findViewById(R.id.pulldown);
		pullDownView.setRefreshListener(this);
		pullDownView.setPullDownElastic(new PullDownElasticImp(getActivity()));
		listView = (ListView) view.findViewById(R.id.listView);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(id<0 || list==null || list.size()<1){
					return;
				}
				Intent i = new Intent(getActivity(),IMActivity.class);
				i.putExtra("userId", list.get((int)id).getUser2());
				i.putExtra("inquiryId", list.get((int)id).getInquiryId());
				startActivity(i);			
				getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);				
			}
		});
		getData();
	}

	
	private void getData(){
		if(MyApplication.Doctor==null){
			getActivity().finish();
			return;
		}
		Cursor cursor = DataSupport.findBySQL("select * from msgitem where user1="+MyApplication.Doctor.getDoctorID()+" group by inquiryid");//,inquiryId");
		if(cursor==null){
			return;
		}
		list.clear();
		MsgItem item = null;
		while(cursor.moveToNext()){
			item = new MsgItem();
			item.setContent(cursor.getString(cursor.getColumnIndex("content")));
			item.setIsSend(cursor.getInt(cursor.getColumnIndex("issend")));
			item.setUser1(cursor.getInt(cursor.getColumnIndex("user1")));
			item.setUser2(cursor.getInt(cursor.getColumnIndex("user2")));
			item.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
			item.setTime(cursor.getLong(cursor.getColumnIndex("time")));
			item.setType(cursor.getInt(cursor.getColumnIndex("type")));
			item.setInquiryId(cursor.getInt(cursor.getColumnIndex("inquiryid")));			
			list.add(item);
		}
		//倒叙输出
		Collections.reverse(list);
		//list = DataSupport.where("jid1='"+app.Doctor.getJID()+"'").find(MsgItem.class);
		listView.setAdapter(new MsgRecordAdapter(getActivity(), list));
	}
	
	@Override
	public void onRefresh(PullDownScrollView view) {
		getData();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {				
				//listView.setAdapter(new MsgRecordAdapter(getActivity(), list));
				pullDownView.finishRefresh(DateFormat.toDateString(Calendar.getInstance().getTimeInMillis(), "hh:mm"));				
			}
		},500);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden){
			getData();
		}
	}
	
	public void refreshView(){
		getData();
	}
	
}
