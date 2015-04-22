/**  
 * @Title: CloudAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年2月20日 下午7:52:54  
 */ 
package cn.net_show.doctor.adapter;

import java.util.ArrayList;

import mark.utils.DateFormat;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.CloudSimpleItem;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * @ClassName: CloudAdapter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月20日 下午7:52:54 
 *  
 */
public class CloudAdapter extends BaseAdapter {
	private Context mContext;
	private AbsListView.LayoutParams lp;
	private TextView tv;
	private ArrayList<CloudSimpleItem> mList;
	/**
	 * @param mContext
	 */
	public CloudAdapter(Context mContext,ArrayList<CloudSimpleItem> list) {
		super();
		mList = list;
		this.mContext = mContext;
		init();
	}

	private void init(){
		lp= new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
		tv=new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setText("没有加载到数据！");
		tv.setLayoutParams(lp);
	}
	
	@Override
	public int getCount() {
		if(mList==null || mList.isEmpty()){
			return 1;
		}
		return mList.size();
	}

	
	@Override
	public Object getItem(int position) {
		if(mList==null || mList.isEmpty()){
			return null;
		}
		return mList.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mList==null || mList.isEmpty()){
			return tv;
		}else{
			ViewHolder holder =null;
			if(convertView==null){
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.cloud_simple_list_item, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.time = (TextView) convertView.findViewById(R.id.modifyTime);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			CloudSimpleItem item = mList.get(position);
			holder.name.setText(item.name);
			holder.time.setText(DateFormat.toDateString(item.lastTime,"MM/dd HH:mm更新了资料"));
		}
		return convertView;
	}
	
	private class ViewHolder{
		TextView name;
		TextView time;
	}

}
