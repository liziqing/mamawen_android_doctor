/**  
 * @Title: UserCenterFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月18日 上午12:42:28  
 */ 
package cn.net_show.doctor.fragment;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.entity.mime.content.FileBody;

import mark.utils.BitmapTool;
import mark.utils.Logger;
import mark.utils.http.HttpUtil;
import mark.utils.http.HttpUtil.ContentPart;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.AuthActivity;
import cn.net_show.doctor.activity.EditUserInfoActivity;
import cn.net_show.doctor.activity.FeeSetActivity;
import cn.net_show.doctor.activity.MyMoneyActivity;
import cn.net_show.doctor.activity.OrderListActivity;
import cn.net_show.doctor.activity.SettingActivity;
import cn.net_show.doctor.activity.SimpleListActivity;
import cn.net_show.doctor.utils.JsonUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @ClassName: UserCenterFragment 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月18日 上午12:42:28 
 *  
 */
public class UserCenterFragment extends Fragment implements View.OnClickListener{
	private View view;
	private MyApplication app;
	private TextView name,doctorId,isAuth;
	private Button btn_auth,btn_fee,money,point;
	private DisplayImageOptions options;
	private ImageButton head;
	private ImageLoader imgLoader;
	private View llt_userinfo;
	private File tempFile;
	private ProgressDialog pd;
	private Handler mHandler;
	private HttpUtil httpUtil;
	private JsonUtils jUtils;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		app = (MyApplication) getActivity().getApplication();
		view = inflater.inflate(R.layout.fragment_user, container, false);	
		init();
		findViews();
		return view;
	}
	private void init(){
		mHandler = new MyHandler(getActivity().getMainLooper());
		httpUtil = HttpUtil.getInstance();
		jUtils = JsonUtils.getInstance();
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_stub)
			.showImageForEmptyUri(R.drawable.ic_empty)
			.showImageOnFail(R.drawable.pic_error)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
		imgLoader = ImageLoader.getInstance();
	}
	private void findViews(){
		btn_auth = (Button) view.findViewById(R.id.btn_auth);
		btn_auth.setOnClickListener(this);
		btn_fee = (Button) view.findViewById(R.id.btn_fee);
		btn_fee.setOnClickListener(this);
		btn_fee = (Button) view.findViewById(R.id.btn_task);
		btn_fee.setOnClickListener(this);
		btn_fee =(Button) view.findViewById(R.id.btn_orders);
		btn_fee.setOnClickListener(this);
		btn_fee =(Button) view.findViewById(R.id.btn_setting);
		btn_fee.setOnClickListener(this);
		name = (TextView) view .findViewById(R.id.txt_name);
		doctorId = (TextView) view .findViewById(R.id.doctorId);
		money = (Button) view .findViewById(R.id.btn_money);
		point = (Button) view .findViewById(R.id.btn_point);
		money.setOnClickListener(this);
		point.setOnClickListener(this);
		isAuth = (TextView) view .findViewById(R.id.txt_isauth);
		head=(ImageButton) view.findViewById(R.id.headImageBtn);
		head.setOnClickListener(this);
		llt_userinfo = view.findViewById(R.id.llt_userInfo);
		llt_userinfo.setOnClickListener(this);
		if(MyApplication.Doctor!=null){
			name.setText(MyApplication.Doctor.getName());
			doctorId.setText(MyApplication.Doctor.getDoctorID()+"");
			if(MyApplication.Doctor.getLevel()<1){
				isAuth.setText("未认证");
			}else if(MyApplication.Doctor.getLevel()==1){
				isAuth.setText("普通认证");
			}else if(MyApplication.Doctor.getLevel()==2){
				isAuth.setText("高级认证");
			}else {//if(MyApplication.Doctor.getLevel()>=3)
				isAuth.setText("签约医生");
			}
			if(MyApplication.Doctor.getAvatar()!=null && !MyApplication.Doctor.getAvatar().trim().equals("")){
				imgLoader.displayImage(MyApplication.ServerUrl+MyApplication.Doctor.getAvatar(), head, options);
			}
			//point.setText(app.Doctor.get);
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch(v.getId()){
		case R.id.btn_auth:
			startActivity(new Intent(getActivity(), AuthActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.llt_userInfo:
			startActivity(new Intent(getActivity(), EditUserInfoActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_fee:
			startActivity(new Intent(getActivity(), FeeSetActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.headImageBtn:
			intent = new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			startActivityForResult(intent, 1111);
			break;
		case R.id.btn_setting:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_orders:
			startActivity(new Intent(getActivity(), OrderListActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_task:
			Toast.makeText(getActivity(), "待完善", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_money:
			startActivity(new Intent(getActivity(), MyMoneyActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_point:
			intent = new Intent(getActivity(), SimpleListActivity.class);
			intent.putExtra("title", "积分明细");
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==1111){
			if(resultCode==Activity.RESULT_OK){
				Uri uri = data.getData();
				if (uri != null) {
					startPhotoZoom(uri);
				}	
			}else{
				return;
			}
		}else if(requestCode==1112){
			if(resultCode!=Activity.RESULT_CANCELED && data!=null){
				
				if(pd == null){
					getProgressDialog();
				}
				pd.show();
				getImageToView(data);
			
			}else{
				tempFile = null;
			}
		}	
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * 调用系统裁剪图片
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 340);
		intent.putExtra("outputY", 340);
		intent.putExtra("return-data", true);
		//tempFile=new File(getActivity().getFilesDir(),System.currentTimeMillis()+".jpg"); // 以时间秒为文件名 File temp = new File("/sdcard/ll1x/");//自已项目 文件夹 if (!temp.exists()) { temp.mkdir(); } inner
		//intent.putExtra("output", Uri.fromFile(tempFile)); // 专入目标文件 
		//intent.putExtra("outputFormat", "JPEG"); //输入文件格式 
		startActivityForResult(intent, 1112);
	}
	/**
	 * 保存裁剪之后的图片数据
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			final Bitmap photo = extras.getParcelable("data");		
			Drawable drawable = new BitmapDrawable(this.getResources(), photo);
			head.setImageDrawable(drawable);
			//imgLoader.displayImage(tempFile.getAbsolutePath(), head,options);
			
			
			new Thread(){
				public void run(){
					uploadHeadImage(photo);
				}
			}.start();
						
		}
	}
	
	
	private void uploadHeadImage(Bitmap photo){
		tempFile=new File(getActivity().getFilesDir(),System.currentTimeMillis()+".jpg");
		BitmapTool.bitmap2File(photo, tempFile);
		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		if(tempFile==null||!tempFile.exists()){
			msg.obj = "获取文件失败";
			mHandler.sendMessage(msg);
			return;
		}
		String url = MyApplication.ServerUrl+"/doctor/avatar/upload?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		ArrayList<ContentPart> list = new ArrayList<>();
		list.add(new ContentPart("avatar", new FileBody(tempFile)));
		String result = httpUtil.mulitiPost(url, list);
		Logger.e("upload", result + "");
		
		if(jUtils.isSuccess(result)){
			msg.obj = "头像上传成功";
		}else{
			msg.obj = "上传头像失败";
		}
		app.Doctor=jUtils.getLoginInfo(result);
		mHandler.sendMessage(msg);
	}
	
	private void getProgressDialog(){
		pd= new ProgressDialog(getActivity());
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("请稍后……");
	}
	
	class MyHandler extends Handler{
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 1:
				if(pd!=null && pd.isShowing()){
					pd.dismiss();
				}
				Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();			
				break;
			}
		}
	}
}
