/**  
 * @Title: ReportAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月23日 上午12:34:20  
 */ 
package cn.net_show.doctor.adapter;

import java.util.List;

import mark.utils.DateFormat;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.InquiryReportItem;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * @ClassName: ReportAdapter 
 * @author 王帅
 * @date 2015年3月23日 上午12:34:20  
 */
public class ReportAdapter extends BaseAdapter {
	private List<InquiryReportItem> mList;
	private Context mContext;
		
	/**
	 * @param mList
	 * @param mContext
	 */
	public ReportAdapter(List<InquiryReportItem> mList, Context mContext) {
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
		if(convertView==null){
			convertView = View.inflate(mContext, R.layout.report_item, null);
			holder = new ViewHolder();
			holder.advice = (TextView) convertView.findViewById(R.id.report_advice);
			holder.info = (TextView) convertView.findViewById(R.id.report_info);
			holder.time = (TextView) convertView.findViewById(R.id.report_time);
			holder.question = (TextView) convertView.findViewById(R.id.report_question);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		InquiryReportItem item = mList.get(position);
		holder.advice.setText(item.repportSuggestion);
		holder.info.setText(item.reportDesc);
		holder.question.setText(item.content);
		holder.time.setText(DateFormat.toDateString(item.createTime,"yyyy.MM.dd"));
		return convertView;
	}
	
	class ViewHolder{
		TextView time;
		TextView info;
		TextView advice;
		TextView question;
	}

}
