/**  
 * @Title: FriendsExpAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年2月17日 上午12:49:01  
 */ 
package cn.net_show.doctor.adapter;

import java.util.List;
import java.util.Locale;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.database.DbUser;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/** 
 * @ClassName: FriendsExpAdapter 
 * @Description: 联系人列表 
 * @author 王帅
 * @date 2015年2月17日 上午12:49:01 
 *  
 */
public class FriendsAdapter extends BaseAdapter {
	private Context mContext;
	private List<DbUser> mList;
	
	private AbsListView.LayoutParams lp;
	private TextView tv;
	
	/**
	 * @param mContext
	 * @param mList
	 */
	public FriendsAdapter(Context mContext, List<DbUser> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
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
		if(mList==null||mList.isEmpty()){
			return null;
		}else{
			return mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mList==null||mList.isEmpty()){
			return tv;
		}
		
		ViewHolder holder = null;
		if(convertView==null){
			convertView = View.inflate(mContext, R.layout.friends_item, null);
			holder = new ViewHolder();
			holder.group_flag = (TextView) convertView.findViewById(R.id.txt_group_flag);
			holder.head = (ImageView) convertView.findViewById(R.id.headImage);
			holder.name = (TextView) convertView.findViewById(R.id.txt_name);
			convertView.setTag(holder);		
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		int section = getSectionForPosition(position);
		
		if(position == getPositionForSection(section)){
			holder.group_flag.setVisibility(View.VISIBLE);
			holder.group_flag.setText(mList.get(position).sortLetter);
		}else{
			holder.group_flag.setVisibility(View.GONE);
		}
		holder.name.setText(mList.get(position).getUserName());
		
		return convertView;
	}
	//获取联系人的拼音分组
	private int getSectionForPosition(int position){
		return mList.get(position).sortLetter.charAt(0);
	}
	/**
	 * 根据分组的拼音查找该组第一条数据出现的位置
	 * @Title: getPositionForSection 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param section
	 * @return   
	 */
	public int getPositionForSection(int section){
		if(mList==null ||mList.isEmpty()){
			return -1;
		}
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).sortLetter;
			char firstChar = sortStr.toUpperCase(Locale.getDefault()).charAt(0);
			if (firstChar == section) {
				return i;
			}
		}		
		return -1;
	}
	
	class ViewHolder{
		TextView group_flag;
		TextView name;
		ImageView head;
	}
	
}
