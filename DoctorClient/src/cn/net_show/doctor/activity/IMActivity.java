/**  
 * @Title: IMActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年2月27日 下午3:52:58  
 */
package cn.net_show.doctor.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mark.utils.ChatMessageListener;
import mark.utils.KeyBoardUtils;
import mark.utils.SDCardUtils;
import mark.utils.SimpleUtils;
import mark.utils.http.HttpUtil;

import org.litepal.crud.DataSupport;

import com.igexin.getuiext.a.i;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.adapter.FastReplyAdapter;
import cn.net_show.doctor.adapter.MsgItemAdapter;
import cn.net_show.doctor.model.FastReplyItem;
import cn.net_show.doctor.model.MsgItem;
import cn.net_show.doctor.model.Role;
import cn.net_show.doctor.model.ServerMsgItem;
import cn.net_show.doctor.model.database.DbInquiry;
import cn.net_show.doctor.model.database.DbUser;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: IMActivity
 * @author 王帅
 * @date 2015年2月27日 下午3:52:58
 */
public class IMActivity extends FragmentActivity implements View.OnClickListener {
	private EditText edt_msg; // 消息输入框
	private Button btn_send, btn_record;//发送 录音
	private ImageButton btn_more;		//更多菜单
	private ImageButton imgBtn_voice;	//录音
	private DbInquiry inquiryItem;	//当前绑定的问诊信息
	private ListView listView, replyListView; // 消息显示列表	
	private List<FastReplyItem> replyList;	  //快捷回复列表
	private List<MsgItem> msgList; 			  // 消息列表
	private MsgItemAdapter adapter; 		  // 消息适配器
	private MyApplication app; 				  // application
	static final String TAG = "IMconnection";		
	private BroadcastReceiver broadcastReceiver;//消息广播接收者
	private MediaRecorder recorder;				//录音管理器
	private File audio;							//录音文件 
	private TextView title, patientInfo;		//标题栏、患者信息栏
	private long timeFlag;						//录音时间戳
	private View menuContainer;					//更多菜单的布局容器
	private Dialog fastReplyDialog;				//快速回复对话框
	private int receiverId,inquiryId;			//交流对象的id 和 绑定的问诊id
	private Role sender,receiver;
	private HttpUtil httpUtil;
	private String serverUrl;
	Handler handler;
	@Override
	protected void onCreate(Bundle saveBundle) {	
		setContentView(R.layout.activity_im);
		super.onCreate(saveBundle);
		init();
		findViews();
	}

	private void getInquiryItem(int inquiryId) {
		if(inquiryId<0){
			inquiryItem = null;
			return;
		}
		List<DbInquiry> list = DataSupport.where("inquiryid = "+inquiryId).find(DbInquiry.class);
		if(list!=null&&list.size()>0){
			inquiryItem = list.get(0);
		}
		
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void init() {
		app = (MyApplication) getApplication();
		//设置音量键控制的音频流
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//广播
		broadcastReceiver = new MessageReciver();
		httpUtil = HttpUtil.getInstance();
		registerReceiver(broadcastReceiver, new IntentFilter(ChatMessageListener.MESSAGE_ACTION));		
		handler = new Handler(getMainLooper());
		msgList = new ArrayList<MsgItem>();	
		receiverId = getIntent().getIntExtra("userId", -1);
		inquiryId = getIntent().getIntExtra("inquiryId", -1);
		msgList = DataSupport.where("user1 = " + MyApplication.Doctor.getDoctorID() + " and user2 = " + receiverId  + " and inquiryId = "+inquiryId).find(MsgItem.class);
		adapter = new MsgItemAdapter(this, msgList);
		getInquiryItem(inquiryId);
		serverUrl = MyApplication.ServerUrl+"/im/doctor/send?uid="+MyApplication.Doctor.getDoctorID()+"&sessionkey="+MyApplication.Doctor.getSessionKey();
		getRoles();
	}

	private void findViews() {
		menuContainer = findViewById(R.id.llt_more_menu);
		btn_more = (ImageButton) findViewById(R.id.imgBtn_more);
		btn_more.setFocusableInTouchMode(true);
		btn_more.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (menuContainer.getVisibility() == View.GONE) {
						KeyBoardUtils.closeKeybord(edt_msg, IMActivity.this);
						menuContainer.setVisibility(View.VISIBLE);
						listView.smoothScrollToPosition(listView.getCount() - 1);
					}
				}
			}
		});
		btn_send = (Button) findViewById(R.id.btn_send);
		imgBtn_voice = (ImageButton) findViewById(R.id.imgBtn_voice);
		btn_record = (Button) findViewById(R.id.btn_record_voice);

		btn_record.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						audio = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),
								MyApplication.Doctor.getJid() + "-"+ System.currentTimeMillis() + ".amr");
						audio.createNewFile();
						timeFlag = System.currentTimeMillis();
						recordVice(audio.getCanonicalPath());
						break;
					case MotionEvent.ACTION_UP:
						if (System.currentTimeMillis() - timeFlag < 1000) {
							Toast.makeText(IMActivity.this, "录音时间过短！",Toast.LENGTH_SHORT).show();
							audio.delete();
							return false;
						}
						recorder.stop();
						recorder.reset();
						recorder.release();
						recorder = null;
						SendFileTask task = new SendFileTask();
						task.execute(audio);
						break;
					}
					return false;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		});
		edt_msg = (EditText) findViewById(R.id.edt_msg);
		edt_msg.requestFocus();
		edt_msg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (menuContainer.getVisibility() == View.VISIBLE) {
						menuContainer.setVisibility(View.GONE);
						// KeyBoardUtils.closeKeybord(edt_msg, IMActivity.this);
					}
				}else{
					KeyBoardUtils.closeKeybord(edt_msg, IMActivity.this);				
				}
			}
		});
		edt_msg.requestFocus();
		edt_msg.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() < 1) {
					imgBtn_voice.setVisibility(View.VISIBLE);
					btn_send.setVisibility(View.GONE);
				} else {
					btn_send.setVisibility(View.VISIBLE);
					imgBtn_voice.setVisibility(View.GONE);
				}

			}
		});
		title = (TextView) findViewById(R.id.title);
		patientInfo = (TextView) findViewById(R.id.txt_patientInfo);
		if(inquiryItem!=null){
			title.setText(inquiryItem.getUserName());
			patientInfo.setText(inquiryItem.getDescription());
		}else{
			title.setText(receiver.getName());
			patientInfo.setText("与患者直接交流中");
		}
		
		
		listView = (ListView) findViewById(R.id.msgListView);
		listView.setAdapter(adapter);
		if (msgList != null && msgList.size() > 1) {
			listView.setSelection(msgList.size());
			listView.smoothScrollToPosition(listView.getCount() - 1);
			
		}
		
	}
	
	private void getRoles(){
		sender = new Role();
		sender.setId(MyApplication.Doctor.getDoctorID());
		sender.setRole(Role.ROLE_DOCTOR);
		DbUser dbUser= DbUser.getDbUserByUserId(receiverId);
		
		if(dbUser==null){
			receiver = new Role();
			receiver.setId(receiverId);
			receiver.setRole(Role.ROLE_USER);
		}else{
			receiver = dbUser.getUserRole();
		}
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.imgBtn_more:
			if (menuContainer.getVisibility() == View.GONE) {
				menuContainer.setVisibility(View.VISIBLE);
				KeyBoardUtils.closeKeybord(edt_msg, this);
				listView.smoothScrollToPosition(listView.getCount() - 1);
			} else {
				menuContainer.setVisibility(View.GONE);
				edt_msg.requestFocus();
			}
			break;
		case R.id.btn_send:
			String msg = edt_msg.getText().toString();
			sendStringMsg(msg);
			break;
		case R.id.imgBtn_voice:
			
			if (btn_record.getVisibility() == View.VISIBLE) {
				btn_record.setVisibility(View.GONE);
				edt_msg.setVisibility(View.VISIBLE);
			} else {
				edt_msg.setVisibility(View.GONE);
				btn_record.setVisibility(View.VISIBLE);
			}
			if (menuContainer.getVisibility() == View.VISIBLE) {
				menuContainer.setVisibility(View.GONE);
			}
			break;
		case R.id.btn_edit:// 编辑快捷回复
			fastReplyDialog.dismiss();
			startActivity(new Intent(this, ReplyEditActivity.class));
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}

	/**
	 * 发送文字信息
	 * 
	 * @Title: sendStringMsg
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	private void sendStringMsg(final String msg) {	
		if (msg == null || msg.trim().length() < 1 || receiverId<1) {
			return;
		}
		MsgItem item = new MsgItem();
		item.setUser1(MyApplication.Doctor.getDoctorID());
		item.setUser2(receiverId);
		item.setTime(System.currentTimeMillis());
		item.setType(MsgItem.TYPE_STRING);
		item.setContent(msg);
		item.setIsSend(MsgItem.SEND);
		item.setStatus(MsgItem.STATUS_READ);
		item.setInquiryId(inquiryId);
		item.save();
		msgList.add(item);
		adapter.notifyDataSetChanged();
		//listView.setSelection(msgList.size());
		listView.smoothScrollToPosition(listView.getCount() - 1);
		edt_msg.setText("");
		//listView.setSelection(msgList.size());
		new Thread(){
			public void run(){
				ServerMsgItem item = new ServerMsgItem();
				item.sender = sender;
				item.receiver = receiver;
				item.category = ServerMsgItem.TYPE_CHAT;
				item.subCategory = ServerMsgItem.SUBTYPE_STRING;
				item.content = msg;
				item.inquiryID = inquiryId;
				String result = httpUtil.sendIMMessage(serverUrl, item);
				Log.e(TAG,result+"----");
			}
		}.start();
	}
	
	
	
	@Override
	protected void onDestroy() {
		// 解除广播注册
		if (broadcastReceiver != null) {
			unregisterReceiver(broadcastReceiver);
		}
		super.onDestroy();
	}

	class SendFileTask extends AsyncTask<File, String, String> {
		private String path;
		//private int type;
		@Override
		protected String doInBackground(File... params) {
			
			path = params[0].getAbsolutePath();
			ServerMsgItem item = new ServerMsgItem();
			item.sender = sender;
			item.receiver = receiver;
			item.category = ServerMsgItem.TYPE_CHAT;
			if (path.endsWith(".amr")) {
				item.subCategory = ServerMsgItem.SUBTYPE_VOICE;
			} else {
				item.subCategory = ServerMsgItem.SUBTYPE_IMAGE;
			}
			//type = item.subCategory;
			item.inquiryID = inquiryId;
			String result = httpUtil.sendIMMessage(serverUrl,item,params[0]);
			Log.e(TAG,result+"----");
			return "文件上传成功！";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(IMActivity.this, result, Toast.LENGTH_LONG).show();
			MsgItem item = new MsgItem();
			item.setUser1(app.Doctor.getDoctorID());
			item.setUser2(receiverId);
			item.setTime(System.currentTimeMillis());
			item.setIsSend(MsgItem.SEND);
			item.setInquiryId(inquiryId);
			item.setStatus(MsgItem.STATUS_READ);
			if (path.endsWith(".amr")) {
				item.setType(MsgItem.TYPE_VOICE);
			} else {
				item.setType(MsgItem.TYPE_IMAGE);
			}
			item.setContent(path);
			item.save();
			msgList.add(item);
			adapter.notifyDataSetChanged();
			//listView.setSelection(msgList.size());
			listView.smoothScrollToPosition(listView.getCount() - 1);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1111:// 选择图片
			if (resultCode == 0) {
				Toast.makeText(this, "没有选中任何文件！", Toast.LENGTH_SHORT).show();
				return;
			}
			Log.e("resultCode", "resultCode=" + resultCode);
			Uri uri = data.getData();
			if (uri != null) {
				sendFile(uri);
			}
			break;
		case 1112:// 拍照
			if (resultCode == RESULT_OK) {
				sendFile(audio);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 发送文件
	 * 
	 * @Title: sendFile
	 * @param uri
	 * @return void
	 */
	private void sendFile(Uri uri) {
		File file = SimpleUtils.Uri2File(this, uri);
		sendFile(file);
	}

	private void sendFile(File file) {
		SendFileTask task = new SendFileTask();
		task.execute(file);
	}

	class MessageReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			MsgItem item = (MsgItem) intent.getExtras().getSerializable("message");
			if (item.getUser2()==receiverId){
				/*if(inquiryId>0 && item.getInquiryId()!=inquiryId){
					return;
				}*/
				msgList.add(item);
				adapter.notifyDataSetChanged();
				//listView.setSelection(msgList.size());
				listView.smoothScrollToPosition(listView.getCount() - 1);				
			}
		}
	}

	/**
	 * 录音
	 * @Title: recordVice
	 * @param path
	 * @return void
	 */
	@SuppressLint("InlinedApi")
	private void recordVice(String path) {
		if (SimpleUtils.isExtraStorageEnable()) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(path);
			// recorder.setAudioSamplingRate();
			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				Toast.makeText(this, "录音失败！", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			} catch (IOException e) {
				Toast.makeText(this, "存储错误,录音失败！", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
			recorder.start();
		} else {
			Toast.makeText(this, "SD卡不可用,无法录音！", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 更多菜单
	 * @Title: onMenuClick
	 * @param v
	 * @return void
	 */
	public void onMenuClick(View v) {
		switch (v.getId()) {
		case R.id.menu_pic:
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			startActivityForResult(intent, 1111);
			break;
		case R.id.menu_camera:
			if (!SDCardUtils.isSDCardEnable()) {
				Toast.makeText(this, "SD卡不可用", Toast.LENGTH_SHORT).show();
				return;
			}
			Intent intent_c = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			audio = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),
					app.Doctor.getJid() + "-" + System.currentTimeMillis()
							+ ".jpg");
			intent_c.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(audio));
			startActivityForResult(intent_c, 1112);
			break;
		case R.id.menu_reply:
			showReplyDialog();
			break;
		case R.id.menu_finish:
			if (inquiryItem != null) {
				Intent intent_finish = new Intent(this, AdviceActivity.class);
				intent_finish.putExtra("data", inquiryItem);
				startActivity(intent_finish);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				Toast.makeText(this, "未知问诊ID", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_history:
			if (inquiryItem != null) {
				Intent i = new Intent(this, ReportActivity.class);
				i.putExtra("data", inquiryItem.getUserID());
				i.putExtra("name", inquiryItem.getUserName());
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				Toast.makeText(this, "未知用户ID", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.menu_reminder:
			Intent i = new Intent(this, ReminderActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			break;
		default:
			Toast.makeText(this, "功能有待完善", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	/**
	 * 获取用户的快捷回复数据
	 * 
	 * @Title: getReplyItem
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	private void getReplyItem() {
		replyList = DataSupport.where("owner='" + app.Doctor.getJid() + "'")
				.order("id desc").find(FastReplyItem.class);
	}
	/**
	 * 快速回复
	 * @Title: showReplyDialog
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	@SuppressLint("ClickableViewAccessibility")
	private void showReplyDialog() {
		getReplyItem();
		if (fastReplyDialog == null) {
			fastReplyDialog = new Dialog(this, R.style.dialog);
			View v = View.inflate(this, R.layout.fast_reply, null);
			Button btn_edit = (Button) v.findViewById(R.id.btn_edit);
			btn_edit.setOnClickListener(this);
			replyListView = (ListView) v.findViewById(R.id.lv_fast_reply);
			replyListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					FastReplyItem item = replyList.get((int)id);
					if(item.getType()==FastReplyItem.TYPE_STRING){
						sendStringMsg(item.getContent());
					}else{
						sendFile(new File(item.getContent()));
					}
					fastReplyDialog.dismiss();
				}
			});
			TextView tv = (TextView) v.findViewById(R.id.emptyView);
			replyListView.setEmptyView(tv);
			fastReplyDialog.addContentView(v, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			fastReplyDialog.setContentView(v);
		}
		replyListView.setAdapter(new FastReplyAdapter(replyList,this));
		fastReplyDialog.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//修改所有消息为已读状态
		if(MyApplication.Doctor!=null){
			ContentValues cv = new ContentValues();
			cv.put("status",1);
			DataSupport.updateAll(MsgItem.class, cv, "status=0 and user1 = " + MyApplication.Doctor.getDoctorID() + " and inquiryId = "+inquiryId);
		}
		super.onPause();
	}
	
	
	
}