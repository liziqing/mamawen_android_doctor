/**  
 * @Title: EditUserInfoActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月29日 下午10:33:32  
 */ 
package cn.net_show.doctor.activity;

import java.io.File;
import java.util.ArrayList;
import org.apache.http.entity.mime.content.FileBody;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import mark.utils.BitmapTool;
import mark.utils.Logger;
import mark.utils.http.HttpUtil;
import mark.utils.http.HttpUtil.ContentPart;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
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
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * @ClassName: EditUserInfoActivity 
 * @author 王帅
 * @date 2015年3月29日 下午10:33:32  
 */
public class EditUserInfoActivity extends FragmentActivity {
	private TextView name,department,hospital,title,goodat,background,achievement,doctorId;
	private MyApplication app;
	private CheckBox serverMore;
	private File tempFile;
	private ProgressDialog pd;
	private Handler mHandler;
	private HttpUtil httpUtil;
	private DisplayImageOptions options;
	private ImageButton head;
	private ImageLoader imgLoader;
	private JsonUtils jUtils;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		app =(MyApplication) getApplication();
		setContentView(R.layout.activity_userinfo_edit);
		init();
		findViews();
	}
	
	private void init(){
		mHandler = new MyHandler(getMainLooper());
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
		name = (TextView) findViewById(R.id.txt_name);
		department = (TextView) findViewById(R.id.txt_department);
		hospital = (TextView) findViewById(R.id.txt_hospital);
		title = (TextView) findViewById(R.id.txt_title);
		goodat = (TextView) findViewById(R.id.txt_goodat);
		achievement = (TextView) findViewById(R.id.txt_achievement);
		background = (TextView) findViewById(R.id.txt_background);
		serverMore = (CheckBox) findViewById(R.id.cb_agree);
		head = (ImageButton) findViewById(R.id.headImageBtn);
		doctorId = (TextView) findViewById(R.id.txt_doctorId);
		
	}
	private void getProgressDialog(){
		pd= new ProgressDialog(this);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("请稍后……");
	}
	
	
	@Override
	protected void onResume() {
		name.setText(app.Doctor.getName());
		department.setText(app.Doctor.getDepartment());
		hospital.setText(app.Doctor.getHospital());
		title.setText(app.Doctor.getTitle());
		goodat.setText(app.Doctor.getGoodAt());
		achievement.setText(app.Doctor.getAchievement());
		background.setText(app.Doctor.getBackground());
		serverMore.setChecked(app.Doctor.isServeMore());
		doctorId.setText(app.Doctor.getDoctorID()+"");
		if(app.Doctor.getAvatar()!=null && !app.Doctor.getAvatar().trim().equals("")){
			imgLoader.displayImage(MyApplication.ServerUrl+app.Doctor.getAvatar(), head, options);
		}
		super.onResume();
	}

	@Override
	public void finish() { 
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_basic:
			startActivity(new Intent(this, EditBasicInfoActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_detail:
			startActivity(new Intent(this, EditMoreInfoActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.headImageBtn:
			Intent intent = new Intent(Intent.ACTION_PICK);		
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			startActivityForResult(intent, 1111);			
			break;
		case R.id.btn_worktime:
			Intent i = new Intent(this, WorkTimeActivity.class);
			i.putExtra("isModify", true);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
		}else{
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/**
	 * 裁剪图片方法实现
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
			tempFile=new File(getFilesDir(),System.currentTimeMillis()+".jpg");
			
			new Thread(){
				public void run(){
					uploadHeadImage(photo,tempFile);
				}
			}.start();
						
		}
	}
	
	
	private void uploadHeadImage(Bitmap photo,File file){
		
		BitmapTool.bitmap2File(photo, file);
		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		if(file==null||!file.exists()){
			msg.obj = "获取文件失败";
			mHandler.sendMessage(msg);
			return;
		}
		String url = MyApplication.ServerUrl+"/doctor/avatar/upload?uid="+app.Doctor.getDoctorID()+"&sessionkey="+app.Doctor.getSessionKey();
		ArrayList<ContentPart> list = new ArrayList<>();
		list.add(new ContentPart("avatar", new FileBody(file)));
		String result = httpUtil.mulitiPost(url, list);
		Logger.e("upload", result + "");
		
		if(jUtils.isSuccess(result)){
			msg.obj = "头像上传成功";
		}else{
			msg.obj = "上传头像失败";
		}
		app.Doctor=jUtils.getLoginInfo(result);
		//app.Doctor.setAvatar(tempFile.getAbsolutePath());
		mHandler.sendMessage(msg);
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
				Toast.makeText(EditUserInfoActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();			
				break;
			}
		}
	}
}
