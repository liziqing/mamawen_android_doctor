/**  
 * @Title: FriendsFragment.java 
 * @Package cn.net_show.doctor.fragment  
 * @author 王帅
 * @date 2015年2月16日 下午11:09:47  
 */
package cn.net_show.doctor.fragment;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import mark.utils.CharacterParser;
import mark.widget.PullDownElasticImp;
import mark.widget.PullDownScrollView;
import mark.widget.SideBar;
import mark.widget.PullDownScrollView.RefreshListener;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.AddPatientActivity;
import cn.net_show.doctor.activity.IMActivity;
import cn.net_show.doctor.adapter.FriendsAdapter;
import cn.net_show.doctor.model.database.DbUser;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @ClassName: FriendsFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月16日 下午11:09:47
 * 
 */
public class FriendsFragment extends Fragment implements RefreshListener,OnClickListener {
	private PullDownScrollView pullDownView;
	private View view, header;
	private ListView listView;
	private TextView tipsTextView;
	private SideBar sideBar;
	private FriendsAdapter adapter;
	private List<DbUser> mList;
	private CharacterParser charParser;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_friends, container, false);
		charParser = CharacterParser.getInstance();
		findViews();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void findViews() {
		pullDownView = (PullDownScrollView) view.findViewById(R.id.pulldown);
		pullDownView.setPullDownElastic(new PullDownElasticImp(getActivity()));
		pullDownView.setRefreshListener(this);
		header = View.inflate(getActivity(), R.layout.friends_head, null);
		Button btn = (Button) header.findViewById(R.id.btn_new_friends);
		btn.setOnClickListener(this);
		
		
		listView = (ListView) view.findViewById(R.id.listView);
		listView.addHeaderView(header);
		// 侧边字母的提示框
		tipsTextView = (TextView) view.findViewById(R.id.dialog);
		// 侧边字母栏
		sideBar = (SideBar) view.findViewById(R.id.sidebar);
		sideBar.setTextView(tipsTextView);
		// 绑定单词选择事件
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				if (null == adapter) {
					return;
				}
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					// 因为有headerView 所以selection的位置要比list中的位置多1
					listView.setSelection(position + 1);
				}
			}
		});
		// 假数据
		bindData();
	}

	/**
	 * 数据转换
	 * 
	 * @Title: convertData
	 * @Description: 给元数据添加排序字母属性
	 * @param array
	 * @return
	 * @return ArrayList<SortUser>
	 */
	private void convertData() {
		if (mList == null) {
			return;
		}
		for (DbUser item : mList) {
			String c = charParser.getSelling(item.getUserName()).substring(0, 1).toUpperCase(Locale.CHINA);
			if (c.matches("[A-Z]")) {
				item.sortLetter = c;
			} else {
				item.sortLetter = "#";
			}
		}
	}

	/**
	 * 绑定数据源
	 * 
	 * @Title: bindData
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	private void bindData() {
		mList = DbUser.getUsers();
		convertData();
		// 对数据源进行排序
		Collections.sort(mList, new Comparator<DbUser>() {
			@Override
			public int compare(DbUser lhs, DbUser rhs) {
				if (lhs.sortLetter.equals("@") || rhs.sortLetter.equals("#")) {
					return -1;
				} else if (lhs.sortLetter.equals("#")
						|| rhs.sortLetter.equals("@")) {
					return 1;
				} else {
					return lhs.sortLetter.compareTo(rhs.sortLetter);
				}
			}
		});
		// Gson gson = new Gson();
		// Log.i("JSON",gson.toJson(sortList));
		adapter = new FriendsAdapter(getActivity(), mList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), IMActivity.class);
				intent.putExtra("userId", mList.get((int)id).getUserId());
				//intent.putExtra("userId", mList.get((int)id).getUserId());
				startActivity(intent);
				
				getActivity().overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				// .overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			}
		});
	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		bindData();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				pullDownView.finishRefresh("上次刷新时间   12:23");
			}		
		}, 1000);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_new_friends:
			startActivity(new Intent(getActivity(), AddPatientActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		}
	}
	
	
	/*private View findViewById(int id){
		if(getView()!=null){
			return getView().findViewById(id);
		}else{
			return null;
		}
	}*/
}
