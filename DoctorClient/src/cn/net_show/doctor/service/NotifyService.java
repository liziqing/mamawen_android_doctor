package cn.net_show.doctor.service;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.activity.BodyActivity;
import cn.net_show.doctor.business.BehindBiz;
import cn.net_show.doctor.utils.SoundPoolUtil;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotifyService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent==null){
			return super.onStartCommand(intent, flags, startId);
		}
		if(intent.getIntExtra("flag", -1)>0){
			showDialog();
			return super.onStartCommand(intent, flags, startId);
		}
		// TODO Auto-generated method stub
		if(intent.getStringExtra("json")!=null){
			sendNotification("妈妈问",intent.getStringExtra("json").toString());
		}else if(intent.getCharSequenceExtra("cid")!=null){
			//上传到服务端  绑定client
			new BehindBiz(this).bindClient(intent.getStringExtra("cid"), null);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@SuppressLint("NewApi")
	private void sendNotification(String title,String content){
		if(content==null){
			content = "测试";
		}
		if(!MyApplication.isNotifyOpen){
			return;
		}
		NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE); 
		Notification.Builder builder = new Notification.Builder(this);  
		
		//builder.setOngoing(true);   
		builder.setAutoCancel(true);
		//builder.
		Intent i = new Intent(this,BodyActivity.class); 
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK); 
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name,  i,  
		        PendingIntent.FLAG_UPDATE_CURRENT); 
		builder.setContentIntent(contentIntent).setSmallIcon(R.drawable.logo);
		builder.setContentTitle(title);
		builder.setContentText(content);
//		if(MyApplication.isNotifyShowDetail){	
//			builder.setSubText("啦啦啦啦啦");
//		}
		if(MyApplication.isNotifyVibrate){
			builder.setVibrate(new long[]{300,100,300,100,300,100,300});
		}
		if(MyApplication.isNotifySound){
			builder.setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.notify) );
		}
		
		builder.setWhen(System.currentTimeMillis());
		nm.notify(title, 1, builder.build());
	
	}
	
	public void showDialog(){
		if(!MyApplication.isNotifyOpen){
			return;
		}
		Log.e("------", "---------");
		final Dialog tipDialog = new Dialog(this,R.style.dialog);
		View v = View.inflate(this, R.layout.custom_dialog, null);
		TextView tv = (TextView) v.findViewById(R.id.dialog_title);
		tv.setText("您有新的付费问诊！");
		tv = (TextView) v.findViewById(R.id.dialog_message);
		tv.setText("您可以到消息记录页面查看此次问诊得记录！");
		Button btn = (Button) v.findViewById(R.id.dialog_btn_p);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NotifyService.this, BodyActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("isShowMsgRecord", true);
				startActivity(intent);
				tipDialog.dismiss();
			}
		});
		btn.setText("前去接诊");
		btn = (Button) v.findViewById(R.id.dialog_btn_n);
		btn.setText("稍后查看");
		btn.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Toast.makeText(NotifyService.this, "您可以在消息记录里找到此次问诊与用户交流！", Toast.LENGTH_SHORT).show();
				tipDialog.dismiss();
			}
		});
		if(MyApplication.isNotifySound){
			SoundPoolUtil.play();
		}
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		//tipDialog.addContentView(v, new LayoutParams((int) (wm.getDefaultDisplay().getWidth()*0.8), LayoutParams.WRAP_CONTENT));
		tipDialog.addContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tipDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
		tipDialog.show();
//		View v = View.inflate(this, R.layout.input_dialog, null);
//		Log.e("------", "22222222");
//	    AlertDialog.Builder b = new AlertDialog.Builder(this);  
//	    Log.e("------", "3333");
//	    b.setView(v);  
//	    Log.e("------", "44444");
//	    AlertDialog d = b.create();   
//	    Log.e("------", "55555");
//	    d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT); 
//	    Log.e("------", "66666");
//	    //d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
//	    d.show();     
//		Log.e("------", "22222222");
	}
}
