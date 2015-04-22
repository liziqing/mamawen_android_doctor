/**  
 * @Title: MsgRecordAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年2月16日 下午10:06:53  
 */
package cn.net_show.doctor.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import mark.utils.DateFormat;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.MsgItem;
import cn.net_show.doctor.model.database.DbUser;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: MsgRecordAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月16日 下午10:06:53
 * 
 */
public class MsgRecordAdapter extends BaseAdapter {
	private Context mContext;
	private AbsListView.LayoutParams lp;
	private TextView tv;
	private List<MsgItem> mList;
	private DisplayImageOptions options;
	private ImageLoader imgLoader;
	public MsgRecordAdapter(Context context, List<MsgItem> list) {
		mContext = context;
		mList = list;
		init();
	}

	/**
	 * 初始化
	 * 
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	private void init() {
		lp = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.MATCH_PARENT);
		tv = new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setText("没有加载到数据！");
		tv.setLayoutParams(lp);
		
		//bmp_error =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_error);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default2)
			.showImageForEmptyUri(R.drawable.default2)
			.showImageOnFail(R.drawable.default2)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
		imgLoader = ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		if (mList == null || mList.size() == 0) {
			return 1;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (mList == null || mList.isEmpty()) {
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		if (mList == null) {
			return -1;
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mList == null || mList.isEmpty()) {
			return tv;
		} else {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.msg_record_item,null);
				holder = new ViewHolder();
				holder.count = (TextView) convertView.findViewById(R.id.txt_unreadFlag);
				holder.header = (ImageView) convertView.findViewById(R.id.headImage);
				holder.lastMsg = (TextView) convertView.findViewById(R.id.lastMsg);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.time = (TextView) convertView.findViewById(R.id.txt_TimeOfLastMsg);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			MsgItem item = mList.get(position);
			int unread = item.getUnReadCount();
			if(unread>0){
				holder.count.setVisibility(View.VISIBLE);
				holder.count.setText(unread + "");
			}else{
				holder.count.setVisibility(View.GONE);
			}
			if(item.getType()==MsgItem.TYPE_IMAGE){
				holder.lastMsg.setText("图片");
			}else if(item.getType()==MsgItem.TYPE_VOICE){
				holder.lastMsg.setText("语言");
			}else{
				holder.lastMsg.setText(item.getContent());
			}
			
			holder.name.setText(getUserNameByUserId(item.getUser2(),holder.header));
			Log.i("time", item.getTime() + "+" + item.getContent());
			holder.time.setText(DateFormat.toDateString(item.getTime(), "HH:mm"));
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView header;
		TextView name;
		TextView time;
		TextView lastMsg;
		TextView count;
	}
	
	private String getUserNameByUserId(int userId,ImageView imgView){
		DbUser user = DbUser.getDbUserByUserId(userId);
		if(user!=null){
			if(user.getAvatar()==null){
				imgView.setImageResource(R.drawable.default_head);
			}else{
				imgLoader.displayImage(MyApplication.ServerUrl+user.getAvatar(),imgView,options);
			}
			return user.getUserName();
		}else{
			imgView.setImageResource(R.drawable.default_head);
			return "获取用户名失败";
		}
	}
}
