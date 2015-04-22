/**  
 * @Title: CaseDetailActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年2月18日 下午5:04:53  
 */ 
package cn.net_show.doctor.activity;

//import java.util.Calendar;
import org.json.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import mark.utils.HttpUtils;
import mark.utils.Logger;
import mark.widget.FlowLayout;
import mark.widget.MyGridView;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.adapter.ImageAdapter;
import cn.net_show.doctor.model.InquiryItem;
import cn.net_show.doctor.model.database.DbInquiry;
import cn.net_show.doctor.model.database.DbUser;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

/** 
 * @ClassName: CaseDetailActivity 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月18日 下午5:04:53 
 *  
 */
public class CaseDetailActivity extends FragmentActivity {
	private GridView gvPic;
	private FlowLayout flowDrug;
	private InquiryItem item;
	private ImageView head;
	private Handler handler;
	private MyApplication app;
	private DisplayImageOptions options;
	private ImageLoader imgLoader;
	private TextView point,name,describe,inquiry_content;
	@Override
	protected void onCreate(Bundle arg0) {
		app = (MyApplication) getApplication();		
		super.onCreate(arg0);
		setContentView(R.layout.activity_case_detail);
		if(getIntent().getSerializableExtra("item") instanceof InquiryItem){
			item = (InquiryItem) getIntent().getSerializableExtra("item");		
		}
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default2)
			.showImageForEmptyUri(R.drawable.default2)
			.showImageOnFail(R.drawable.default2).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20)).build();
		imgLoader = ImageLoader.getInstance();
		handler = new Handler(getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					Toast.makeText(CaseDetailActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(CaseDetailActivity.this, "该问诊已被接诊！", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Intent i = new Intent(CaseDetailActivity.this, IMActivity.class);
					i.putExtra("userId", item.user.userID);
					i.putExtra("inquiryId", item.id);
					DbInquiry dbInuiry = DbInquiry.getDbModel(item);
					dbInuiry.save();
//					if(!DbUser.isUserExsits(item.user.userID)){
//						DbUser.getDbUserByUser(item.user).save();
//					}
					DbUser.saveIfNotExist(item.user);
					Toast.makeText(CaseDetailActivity.this, "接诊成功！", Toast.LENGTH_SHORT).show();
					//sendToPatient(item);				
					startActivity(i);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
					finish();
					break;
				default:
					break;
				}
			}
			
		};
		findViews();
		
	}
	
	private void findViews(){
		gvPic=(MyGridView) findViewById(R.id.gridView_pic);
		flowDrug =  (FlowLayout) findViewById(R.id.flowLayout);
		gvPic.setAdapter(new ImageAdapter(this,item.photoes));
		head = (ImageView) findViewById(R.id.headImage);
		point = (TextView) findViewById(R.id.point);
		point.setVisibility(View.GONE);
		point = (TextView) findViewById(R.id.jifen);
		point.setVisibility(View.GONE);
		point = (TextView) findViewById(R.id.txt_point);
		name = (TextView) findViewById(R.id.name);
		describe = (TextView) findViewById(R.id.describe);
		inquiry_content = (TextView) findViewById(R.id.inquiry_content);
		
		if(item!=null){
			imgLoader.displayImage(MyApplication.ServerUrl+item.user.avator, head,options);
			point.setText("+"+item.point);
			name.setText(item.user.userName);
			inquiry_content.setText(item.content);			
			describe.setText(item.description);
			gvPic.setAdapter(new ImageAdapter(this, item.photoes));
			if(item.drug!=null && !item.drug.trim().equals("")){
				showDrags(item.drug.split("\\$"));
			}
		}
	}
	
	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_submit:
			accept();
			/*Intent i = new Intent(CaseDetailActivity.this, IMActivity.class);
			i.putExtra("item", item);
			Toast.makeText(CaseDetailActivity.this, "接诊成功！", Toast.LENGTH_SHORT).show();
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
			break;
		default:
			break;
		}
	}
	
	@Override
	public void finish() {		
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
	
	
	private void accept(){
		new Thread(){
			public void run(){
				if(item!=null){
					try {//http://115.159.49.31:9000/inquiry/recept?uid=2&sessionkey=123
						String url = MyApplication.ServerUrl+"/inquiry/recept?uid="+app.Doctor.getDoctorID()+"&sessionkey=456";
						
						JSONObject obj = new JSONObject();
						obj.put("inquiryID", item.id);		
						Log.e("url","url="+url +"\n"+obj.toString());
						String result = HttpUtils.doPost(url, obj.toString());
						Logger.e("确认接诊json", result);
						if(result==null || result.length()<1){
							Logger.e("确认接诊", "网络异常");
							handler.sendEmptyMessage(1);
							return;
						}
						JSONObject  obj2 = new JSONObject(result);
						int code = obj2.getInt("code");
						
						Logger.e("确认接诊", obj2.getString("message"));
						if(code!=0){
							handler.sendEmptyMessage(2);
							return;
						}
						handler.sendEmptyMessage(3);
					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(1);
					}
				}
			}
		}.start();	
	}
	
	private void showDrags(String[] array){
		if(array==null || array.length<1){
			return;
		}
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 15, 10, 15);
		TextView tv = null;
		for(String str : array){
			tv = new TextView(this);
			tv.setText(str);
			//tv.setTextSize(20);
			tv.setTextColor(Color.WHITE);
			tv.setPadding(15, 10, 15, 10);
			tv.setBackgroundResource(R.drawable.rdconner_blue);
			tv.setLayoutParams(params);
			flowDrug.addView(tv);
		}
	}
	
//	private void test(){
//		String[] array = {"屌丝","程序员","呵呵哈哈","天空飘来五个字","屌丝","程序员","呵呵哈哈","天空飘来五个字"};
//		showDrags(array);
//	}
	
//	private void sendToPatient(final InquiryItem item){		
//		new Thread(){
//			public void run(){
//				Role sender = new Role();
//				sender.setId(app.Doctor.getDoctorID());
//				//sender.setName(app.Doctor.getName());
//				sender.setRole(Role.ROLE_DOCTOR);
//				
//				Role reciever = new Role();
//				reciever.setId(item.user.userID);
//				reciever.setRole(Role.ROLE_USER);
//				HttpUtil httpUtil = HttpUtil.getInstance();
//				ServerMsgItem sItem = new ServerMsgItem();
//				sItem.sender = sender;
//				sItem.receiver = reciever;
//				sItem.category = ServerMsgItem.TYPE_CHAT;
//				sItem.inquiryID = item.id;
//				sItem.subCategory = ServerMsgItem.SUBTYPE_STRING;
//				sItem.content = app.Doctor.getName()+"医生为您接诊";
//				String result = httpUtil.sendIMMessage(serverUrl, sItem);
//				Logger.e("接诊",result+"----");
//			}
//		}.start();
//		
//	}
}
