/**  
 * @Title: FastReplyAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月22日 下午1:54:08  
 */ 
package cn.net_show.doctor.adapter;

import java.util.List;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.FastReplyItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * @ClassName: FastReplyAdapter 
 * @author 王帅
 * @date 2015年3月22日 下午1:54:08  
 */
public class FastReplyAdapter extends BaseAdapter{
	private List<FastReplyItem> mList;
	private Context mContext;
	/**
	 * @param mList
	 * @param mContext
	 */
	public FastReplyAdapter(List<FastReplyItem> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if(mList==null){
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,R.layout.fast_reply_item, null);
			holder.subject = (TextView) convertView.findViewById(R.id.subject);
			holder.detail = (TextView) convertView.findViewById(R.id.detail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		FastReplyItem item = mList.get(position);
		holder.subject.setText(item.getSubject());
		if (item.getType() == FastReplyItem.TYPE_STRING) {
			holder.detail.setText(item.getContent());
		} else {
			holder.detail.setText("");
		}
		return convertView;
	}

	class ViewHolder {
		TextView subject;
		TextView detail;
	}

}
