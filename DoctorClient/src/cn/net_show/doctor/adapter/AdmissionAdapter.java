/**  
 * @Title: InquiryAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年2月16日 上午12:53:43  
 */ 
package cn.net_show.doctor.adapter;

import java.util.List;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.InquiryItem;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * @ClassName: InquiryAdapter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月16日 上午12:53:43 
 *  
 */
public class AdmissionAdapter extends BaseAdapter {
	private List<InquiryItem> mList;
	private Context mContext;
	private AbsListView.LayoutParams lp;
	private TextView tv;
	private OnItemClikListener listener;
	private ViewHolder holder;
	public AdmissionAdapter(Context context,List<InquiryItem> list){
		mContext = context;
		mList = list;
		lp= new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
		tv=new TextView(mContext);
		tv.setGravity(Gravity.CENTER);
		tv.setText("没有加载到数据！");
		tv.setLayoutParams(lp);
	}
	@Override
	public int getCount() {
		if(mList==null || mList.size()==0){
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
		if(mList==null || mList.size()<1){
			return -1;
		}
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mList==null || mList.isEmpty()){
			return tv;
		}else{
			if(convertView==null){
				convertView = View.inflate(mContext, R.layout.admission_summary_item,null);
				holder = new ViewHolder();
				holder.content = (TextView) convertView.findViewById(R.id.inquiry_content);
				holder.head = (ImageView) convertView.findViewById(R.id.headImage);
				holder.describe = (TextView) convertView.findViewById(R.id.describe);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.point = (TextView) convertView.findViewById(R.id.point);
				//holder.time = (TimeTextView) convertView.findViewById(R.id.time);
				//holder.imageCount = (TextView) convertView.findViewById(R.id.imgInfo);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
				if(holder == null ){
					convertView = View.inflate(mContext, R.layout.admission_summary_item,null);
					holder = new ViewHolder();
					holder.content = (TextView) convertView.findViewById(R.id.inquiry_content);
					holder.head = (ImageView) convertView.findViewById(R.id.headImage);
					holder.describe = (TextView) convertView.findViewById(R.id.describe);
					holder.name = (TextView) convertView.findViewById(R.id.name);
					holder.point = (TextView) convertView.findViewById(R.id.point);
					//holder.time = (TimeTextView) convertView.findViewById(R.id.time);
					//holder.imageCount = (TextView) convertView.findViewById(R.id.imgInfo);
					convertView.setTag(holder);
				}
			}
			
			final InquiryItem temp = mList.get(position);
			/*convertView.setClickable(true);
			if(listener!=null){
				convertView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						listener.onClick(temp);
					}
				});
			}*/
			holder.content.setText(temp.content);
			holder.describe.setText(temp.description);
			//holder.imageCount.setText("附图 "+temp.AccessoryImageCount);
			holder.name.setText(temp.user.userName);
			holder.point.setText("+"+temp.point);			
		}
		
		return convertView;
	}
	private class ViewHolder{
		ImageView head;
		TextView name;
		TextView describe;
		TextView point;
		TextView content;
		//TimeTextView time;
		//TextView imageCount;
	}
	public void setOnItemClick(OnItemClikListener listener){
		this.listener = listener;
	}
	public interface OnItemClikListener{
		public void onClick(InquiryItem item);
	}
}
