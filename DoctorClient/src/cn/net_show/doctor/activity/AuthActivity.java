/**  
 * @Title: AuthActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月21日 下午12:08:37  
 */
package cn.net_show.doctor.activity;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.entity.mime.content.FileBody;

import com.nostra13.universalimageloader.core.ImageLoader;

import mark.utils.Logger;
import mark.utils.SimpleUtils;
import mark.utils.http.HttpUtil;
import mark.utils.http.HttpUtil.ContentPart;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.utils.JsonUtils;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: AuthActivity
 * @author 王帅
 * @date 2015年3月21日 下午12:08:37
 */
public class AuthActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{
	private ImageButton authImage;
	private Uri uri;
	private MyApplication app;
	private HttpUtil httpUtil;
	private boolean isUpload = false;
	private Handler mHandler;
	private JsonUtils jUtils;
	private RadioGroup rdgroup;
	private ProgressDialog pd;
	private TextView type;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		app = (MyApplication) getApplication();
		httpUtil = HttpUtil.getInstance();
		jUtils = JsonUtils.getInstance();
		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if(pd!=null & pd.isShowing()){
						pd.dismiss();
					}
					Toast.makeText(AuthActivity.this, msg.obj.toString(),
							Toast.LENGTH_SHORT).show();
					break;
				case 2:

					break;
				default:

					break;
				}
			}

		};
		setContentView(R.layout.activity_auth);
		rdgroup = (RadioGroup) findViewById(R.id.radiogroup);
		authImage = (ImageButton) findViewById(R.id.authImage);
		rdgroup.setOnCheckedChangeListener(this);
		type = (TextView) findViewById(R.id.tv_type);
		super.onCreate(arg0);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.authImage:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, 1111);
			break;
		case R.id.btn_auth:
			if (isUpload) {
				Toast.makeText(this, "图片上传中，请稍后！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (uri == null) {
				Toast.makeText(this, "请选择照片！", Toast.LENGTH_SHORT).show();
				return;
			} else {
				if(pd==null){
					getProcessDialog();
				}
				pd.show();
				String key = "";
				if(rdgroup.getCheckedRadioButtonId()==R.id.rd_qw){
					key = "license";
				}else{
					key = "plate";
				}
				auth(key);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1111:
			if (resultCode == 0) {
				// uri = null;
				//Toast.makeText(this, "没有选中任何文件！", Toast.LENGTH_SHORT).show();
				return;
			}
			Log.e("resultCode", "resultCode=" + resultCode);
			uri = data.getData();
			//authImage.setImageURI(uri);
			ImageLoader.getInstance().displayImage(uri.toString(),authImage);
			break;
		default:
			break;
		}
	}

	private void auth(final String key) {
		new Thread() {
			public void run() {
				isUpload = true;
				Message msg = mHandler.obtainMessage();
				msg.what = 1;
				ArrayList<ContentPart> list = new ArrayList<>();
				File file = SimpleUtils.Uri2File(AuthActivity.this, uri);
				if (file == null || !file.exists()) {
					Log.e("upload", "file path error!");
					isUpload = false;
					msg.obj = "没有找到照片文件！";
					mHandler.sendMessage(msg);
					return;
				}
				ContentPart part = new ContentPart(key, new FileBody(file));
				list.add(part);
				// part = new ContentPart("license", new FileBody(null));
				// list.add(part);
				String url = MyApplication.ServerUrl + "/doctor/level/authen?uid="
						+ app.Doctor.getDoctorID() + "&sessionkey="
						+ app.Doctor.getSessionKey();
				String result = httpUtil.mulitiPost(url, list);
				Logger.e("upload", result + "");
				if (result == null) {
					msg.obj = "上传照片失败！";
				} else {
					if (jUtils.isSuccess(result)) {
						msg.obj = "上传照片成功！";
					} else {
						msg.obj = "上传照片失败！";
					}
				}
				mHandler.sendMessage(msg);
				isUpload = false;
			}
		}.start();
		
	}
	
	private void getProcessDialog(){
		pd = new ProgressDialog(this);//, R.style.dialog);
		pd.setCancelable(false);
		pd.setCanceledOnTouchOutside(false);
		pd.setMessage("上传中……");
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.rd_pt:
			type.setText("上传胸牌");
			break;
		case R.id.rd_qw:
			type.setText("上传资格证或职业证");
			break;
		default:
			break;
		}
	}
}
