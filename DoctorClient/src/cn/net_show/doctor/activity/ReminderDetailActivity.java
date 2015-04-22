/**  
 * @Title: ReminderDetailActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月5日 下午12:12:19  
 */ 
package cn.net_show.doctor.activity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import mark.utils.DateFormat;
import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import mark.utils.Logger;
import com.google.gson.Gson;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.ReminderItem;
import cn.net_show.doctor.model.database.DbUser;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/** 
 * @ClassName: ReminderDetailActivity 
 * @author 王帅
 * @date 2015年3月5日 下午12:12:19  
 */
public class ReminderDetailActivity extends FragmentActivity {
	private boolean isModify = false;
	private TextView subject,time,userName,times,dialog_title,currentTextView;
	private CheckBox rm_self,rm_other;
	private EditText edt_content,input;
	private Dialog inputDialog;
	private Calendar selectedCal;
	private DbUser user;
	private ReminderItem rItem;
	private ProgressDialog pd;
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_reminder_detail);
		rItem = (ReminderItem) getIntent().getSerializableExtra("data");
		mHandler = new Handler(getMainLooper());
		findViews();
		if(rItem!=null){
			isModify = true;
			subject.setText(rItem.getTitle());
			time.setText(rItem.getRemindTime());
			userName.setText(rItem.getPatientName());
			rm_self.setChecked(rItem.isRemindMe());
			edt_content.setText(rItem.getContent());
		}else{
			rItem = new ReminderItem();
		}
		
	}
	private void findViews(){
		subject = (TextView) findViewById(R.id.txt_detail_title);
		time = (TextView) findViewById(R.id.txt_detail_time);
		userName = (TextView) findViewById(R.id.txt_detail_patient);
		times = (TextView) findViewById(R.id.txt_detail_times);
		rm_self = (CheckBox) findViewById(R.id.cb_remind_self);
		rm_other = (CheckBox) findViewById(R.id.cb_remind_other);
		edt_content = (EditText) findViewById(R.id.edt_content);
		selectedCal = Calendar.getInstance(Locale.getDefault());
		time.setText(DateFormat.toDateString(selectedCal.getTimeInMillis(), "yyyy-MM-dd HH:mm"));
		getInputDialog();
	}
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_save:
			saveToServer();
			break;
		case R.id.reminder_detail_title:
			currentTextView = subject;
			dialog_title.setText("主题");
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			if(inputDialog!=null && !inputDialog.isShowing()){
				inputDialog.show();
				input.setText(currentTextView.getText());
			}
			break;
		case R.id.reminder_detail_time:
			showDateDialog();
			break;
		case R.id.reminder_detail_patient:
			userSelected();
			break;
		case R.id.reminder_detail_times:
			currentTextView = times;
			dialog_title.setText("重复天数");
			input.setInputType(InputType.TYPE_CLASS_NUMBER);
			if(inputDialog!=null && !inputDialog.isShowing()){
				inputDialog.show();
				input.setText(currentTextView.getText());
			}
			break;
		default :
			break;
		}
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void finish() {		
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
		//overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
//	{
//	"title":"title",
//	"patientName": "Jack",
//	"content": "content",
//	"startDate":"2015-04-01",//务必上传这种格式
//	"endDate":"2015-04-21",//务必上传这种格式
//	"remindTime":"10:00:00",//务必上传这种格式
//	"remindMe": false,
//	"doctorID": 1,
//	"userID": 1 //如果是医生提醒自己可以不填
//	}
	private void saveToServer(){
		if(MyApplication.Doctor==null){
			return;
		}
		if(user==null && !isModify){
			Toast.makeText(this, "请先添加患者", Toast.LENGTH_SHORT).show();
			return;
		}
		int int_times = Integer.parseInt(times.getText().toString());
		if(int_times<0){
			return;
		}
		String str_title = subject.getText().toString().trim();
		if(user==null){
			
		}else{
			String str_patinetName = user.getUserName();
			int userID = user.getUserId();
			rItem.setUserID(userID);
			rItem.setPatientName(str_patinetName);
		}
		
		String str_content = edt_content.getText().toString();
		if(str_title.equals("")||str_content.equals("")){
			Toast.makeText(this, "请将内容填写完整", Toast.LENGTH_SHORT).show();
			return;
		}
		String startDate = DateFormat.toDateString(selectedCal.getTimeInMillis(), "yyyy-MM-dd");
		selectedCal.add(Calendar.DAY_OF_MONTH, int_times);
		String endDate  = DateFormat.toDateString(selectedCal.getTimeInMillis(), "yyyy-MM-dd");
		String remindTime = DateFormat.toDateString(selectedCal.getTimeInMillis(), "HH:mm:00");
		int doctorID = MyApplication.Doctor.getDoctorID();
		boolean remindMe = rm_self.isChecked();
		
		rItem.setContent(str_content);
		rItem.setDoctorID(doctorID);
		rItem.setEndDate(endDate);
		rItem.setRemindMe(remindMe);
		rItem.setRemindTime(remindTime);
		rItem.setStartDate(startDate);
		rItem.setTitle(str_title);
		
		//rItem.setReminderID(reminderID);
		
		String serverUrl = MyApplication.ServerUrl;
		if(isModify){
			serverUrl += "/doctor/reminder/update"; 
		}else{
			serverUrl += "/doctor/reminder/add";
		}
		
		showPd();
		serverUrl = serverUrl+ "?uid="+MyApplication.Doctor.getDoctorID()+"&sessionkey="+MyApplication.Doctor.getSessionKey();
		Gson gson = new Gson();
		Logger.e("json",gson.toJson(rItem));
		HttpUtils.doPostAsyn(serverUrl, gson.toJson(rItem), new CallBack() {		
			@Override
			public void onRequestComplete(String result) {
				mHandler.post(new Runnable() {					
					@Override
					public void run() {
						pd.dismiss();
					}
				});
			}
		});	
	}
	
	private void getInputDialog(){
		inputDialog = new Dialog(this,R.style.dialog);
		View v = View.inflate(this, R.layout.input_dialog, null);
		dialog_title = (TextView) v.findViewById(R.id.dialog_title);
		input = (EditText) v.findViewById(R.id.edt_input);
		Button btn = (Button) v.findViewById(R.id.dialog_btn_p);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentTextView.setText(input.getText().toString());
				inputDialog.dismiss();
			}
		});
		btn = (Button) v.findViewById(R.id.dialog_btn_n);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				inputDialog.dismiss();
			}
		});
		inputDialog.addContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		inputDialog.setContentView(v);
	}
	
	private void showDateDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("时间选择");
		View view = View.inflate(this, R.layout.date_time_picker, null);
		final DatePicker dp = (DatePicker) view.findViewById(R.id.datepicker);
		final TimePicker tp = (TimePicker) view.findViewById(R.id.timepicker);
		tp.setIs24HourView(true);
		builder.setView(view);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, dp.getYear());
				cal.set(Calendar.MONTH, dp.getMonth());
				cal.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
				cal.set(Calendar.HOUR_OF_DAY, tp.getCurrentHour());
				cal.set(Calendar.MINUTE, tp.getCurrentMinute());
				selectedCal = cal;
				time.setText(DateFormat.toDateString(cal.getTimeInMillis(), "yyyy-MM-dd HH:mm"));
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();	
	}
	
	private void userSelected(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择患者");
		final List<DbUser>  list = DbUser.getUsers();
		if(list==null){
			Toast.makeText(this, "请先添加患者", Toast.LENGTH_SHORT).show();
			return;
		}
		builder.setAdapter(new ArrayAdapter<DbUser>(this,android.R.layout.simple_list_item_1,list), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				user = list.get(which);
				userName.setText(user.getUserName());
			}
		});
		builder.create().show();
	}
	
	private void showPd(){
		if(pd==null){
			pd = new ProgressDialog(this);
			pd.setMessage("请稍后……");
			pd.setCancelable(false);
		}
		pd.show();
	}
	
//	26  添加提醒
//	method: post
//	url: /doctor/reminder/add
//	请求：
//	{
//	"title":"title",
//	"patientName": "Jack",
//	"content": "content",
//	"startDate":"2015-04-01",//务必上传这种格式
//	"endDate":"2015-04-21",//务必上传这种格式
//	"remindTime":"10:00:00",//务必上传这种格式
//	"remindMe": false,
//	"doctorID": 1,
//	"userID": 1 //如果是医生提醒自己可以不填
//	}

	
//	29  修改提醒内容
//	method: post
//	url: /doctor/reminder/update?uid=xxx&sessionkey=xxxx
//	请求：
//	{
//	"reminderID": 1,
//	"title": "title",
//	"patientName": "Jack",
//	"content": "content",
//	"startDate": "2015-04-01",
//	"endDate": "2015-04-21",
//	"remindTime": "10:00:00",
//	"doctorID": 1,
//	"remindMe": false,
//	"userID": 1
//	}
}
