/**  
 * @Title: DataRecordAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月5日 上午10:44:58  
 */
package cn.net_show.doctor.adapter;

import java.util.ArrayList;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.FriendsRecord;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: DataRecordAdapter
 * @author 王帅
 * @date 2015年3月5日 上午10:44:58
 */
public class DataRecordAdapter extends BaseAdapter {

	private ArrayList<FriendsRecord> mList;
	private Context mContext;

	private AbsListView.LayoutParams lp;
	private TextView tv;

	/**
	 * @param mList
	 * @param mContext
	 */
	public DataRecordAdapter(ArrayList<FriendsRecord> list, Context context) {
		super();
		this.mList = list;
		this.mContext = context;
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
			convertView = View.inflate(mContext, R.layout.data_record_item,
					null);
			holder.name = (TextView) convertView.findViewById(R.id.txt_name);
			holder.time = (TextView) convertView.findViewById(R.id.txt_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FriendsRecord item = mList.get(position);

		holder.name.setText(item.friend.name);
		holder.time.setText(item.lastRecord.updatedTime);

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView time;
		ImageView head;
	}
}
