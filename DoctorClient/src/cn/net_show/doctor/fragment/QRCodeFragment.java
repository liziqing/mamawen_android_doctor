/**  
 * @Title: QRCodeFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月20日 下午6:03:17  
 */
package cn.net_show.doctor.fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @ClassName: QRCodeFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月20日 下午6:03:17
 * 
 */
public class QRCodeFragment extends Fragment {
	private View view;
	private DisplayImageOptions options;
	private ImageLoader imgLoader;
	private ImageView header;
	private TextView doctorId,name,title;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default2)
			.showImageForEmptyUri(R.drawable.default2)
			.showImageOnFail(R.drawable.default2).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20)).build();
		imgLoader = ImageLoader.getInstance();
		view = inflater.inflate(R.layout.qrcode, container, false);
		findViews();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void findViews(){
		header = (ImageView) view.findViewById(R.id.headImage);
		doctorId = (TextView) view.findViewById(R.id.txt_id);
		title = (TextView) view.findViewById(R.id.txt_post);
		name = (TextView) view.findViewById(R.id.txt_name);
		if(MyApplication.Doctor!=null){
			imgLoader.displayImage(MyApplication.ServerUrl+MyApplication.Doctor.getAvatar(), header, options);
			title.setText(MyApplication.Doctor.getTitle());
			name.setText(MyApplication.Doctor.getName());
			doctorId.setText(MyApplication.Doctor.getDoctorID()+"");
		}
	}
}
