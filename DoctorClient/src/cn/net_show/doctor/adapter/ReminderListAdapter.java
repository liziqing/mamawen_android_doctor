/**  
 * @Title: ReminderListActivity.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月5日 下午1:40:32  
 */
package cn.net_show.doctor.adapter;

import java.util.ArrayList;

import mark.utils.DateFormat;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.ReminderDetailActivity;
import cn.net_show.doctor.model.ReminderItem;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @ClassName: ReminderListActivity
 * @author 王帅
 * @date 2015年3月5日 下午1:40:32
 */
public class ReminderListAdapter extends BaseAdapter {
	private ArrayList<ReminderItem> mList;
	private Context mContext;

	private AbsListView.LayoutParams lp;
	private TextView tv;

	/**
	 * @param mList
	 * @param mContext
	 */
	public ReminderListAdapter(ArrayList<ReminderItem> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		init();
	}

	private void init() {
		lp = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.MATCH_PARENT);
		tv = new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setText("没有加载到数据！");
		tv.setLayoutParams(lp);
	}

	@Override
	public int getCount() {
		if (mList == null || mList.isEmpty()) {
			return 1;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mList == null || mList.isEmpty()) {
			return null;
		} else {
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mList == null || mList.isEmpty()) {
			return tv;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.reminder_list_item,null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ReminderItem item = mList.get(position);
		holder.content.setText(item.getContent());
		holder.time.setText(item.getRemindTime());
		holder.name.setText(item.getPatientName());
		return convertView;
	}

	class ViewHolder {
		TextView content;
		TextView time;
		TextView name;
	}
}
