/**  
 * @Title: DataRecordAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月5日 上午10:44:58  
 */
package cn.net_show.doctor.adapter;

import java.util.ArrayList;

import mark.utils.DateFormat;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.DataRecordItem;
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

	private ArrayList<DataRecordItem> mList;
	private Context mContext;

	private AbsListView.LayoutParams lp;
	private TextView tv;

	/**
	 * @param mList
	 * @param mContext
	 */
	public DataRecordAdapter(ArrayList<DataRecordItem> list, Context context) {
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
		DataRecordItem item = mList.get(position);

		holder.name.setText(item.name);
		holder.time.setText(DateFormat.getFriendlyTimeString(item.time));

		return convertView;
	}

	class ViewHolder {
		TextView name;
		TextView time;
		ImageView head;
	}
}
