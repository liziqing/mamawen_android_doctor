/**  
 * @Title: ReminderActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月4日 下午9:25:51  
 */
package cn.net_show.doctor.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.litepal.crud.DataSupport;

import mark.utils.DateFormat;
import mark.utils.HttpUtils;
import mark.utils.HttpUtils.CallBack;
import mark.utils.SimpleUtils;
import mark.widget.CalendarView;
import mark.widget.CalendarView.OnItemClickListener;
import mark.widget.PullDownElasticImp;
import mark.widget.PullDownScrollView;
import mark.widget.PullDownScrollView.RefreshListener;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.adapter.ReminderListAdapter;
import cn.net_show.doctor.model.ReminderItem;
import cn.net_show.doctor.service.NotifyService;
import cn.net_show.doctor.utils.JsonUtils;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @ClassName: ReminderActivity
 * @author 王帅
 * @date 2015年3月4日 下午9:25:51
 */
@SuppressLint("InflateParams")
public class ReminderActivity extends FragmentActivity implements
		RefreshListener {
	private PullDownScrollView pullDownView;
	private ListView listView;
	private View header;
	private ArrayList<ReminderItem> mList;
	private ReminderListAdapter adapter;
	private MyApplication app;
	private JsonUtils jUtils;
	private Handler mHandler;
	private CalendarView calendar;
	private ImageButton calendarLeft;
	private TextView calendarCenter;
	private ImageButton calendarRight;
	private SimpleDateFormat format;

	@Override
	protected void onCreate(Bundle bundle) {
		app = (MyApplication) getApplication();
		jUtils = JsonUtils.getInstance();
		super.onCreate(bundle);
		setContentView(R.layout.activity_reminder);
		initViews();
		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				pullDownView.finishRefresh("上次刷新时间   "+DateFormat.toDateString(Calendar.getInstance().getTimeInMillis(), "HH:mm"));
				switch (msg.what) {
				case 1:
					saveRemindList(mList);
					adapter = new ReminderListAdapter(filiter(format.format(calendar.getSelectedStartDate())), ReminderActivity.this);
					listView.setAdapter(adapter);
					//adapter = new ReminderListAdapter(mList,ReminderActivity.this);
					//listView.setAdapter(adapter);
					break;
				case 2:
					Toast.makeText(ReminderActivity.this, "获取数据失败",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}

		};
	}

	public void initViews() {
		header = View.inflate(this, R.layout.header_calender, null);
		format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		// 获取日历控件对象
		calendar = (CalendarView) header.findViewById(R.id.calendar);
		calendar.setSelectMore(false); // 单选

		calendarLeft = (ImageButton) header.findViewById(R.id.calendarLeft);
		calendarCenter = (TextView) header.findViewById(R.id.calendarCenter);
		calendarRight = (ImageButton) header.findViewById(R.id.calendarRight);
		try {
			// 设置日历日期
			Date date = format.parse("2015-01-01");
			calendar.setCalendarData(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		String[] ya = calendar.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		calendarLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击上一月 同样返回年月
				String leftYearAndmonth = calendar.clickLeftMonth();
				String[] ya = leftYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		calendarRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 点击下一月
				String rightYearAndmonth = calendar.clickRightMonth();
				String[] ya = rightYearAndmonth.split("-");
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
		});

		// 设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
		calendar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void OnItemClick(Date selectedStartDate,
					Date selectedEndDate, Date downDate) {
				if (calendar.isSelectMore()) {
					Toast.makeText(
							getApplicationContext(),
							format.format(selectedStartDate) + "到"
									+ format.format(selectedEndDate),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),format.format(downDate), Toast.LENGTH_SHORT).show();
					adapter = new ReminderListAdapter(filiter(format.format(downDate)), ReminderActivity.this);
					listView.setAdapter(adapter);
					startService(new Intent(ReminderActivity.this,NotifyService.class));
				}
			}
		});
		pullDownView = (PullDownScrollView) findViewById(R.id.pulldown);
		listView = (ListView) findViewById(R.id.listView);
		listView.addHeaderView(header);
		listView.setDividerHeight(SimpleUtils.dp2px(this, 2));
		getRemainderList();
		//adapter = new ReminderListAdapter(mList, ReminderActivity.this);
		//listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ReminderActivity.this,
						ReminderDetailActivity.class);
				//intent.putExtra("data", mList.get((int)id));
				intent.putExtra("data", (ReminderItem)adapter.getItem((int)id));
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
			}
		});

		pullDownView.setRefreshListener(this);
		pullDownView.setPullDownElastic(new PullDownElasticImp(this));

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_add_reminder:
			startActivity(new Intent(ReminderActivity.this,
					ReminderDetailActivity.class));

			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			// overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
			break;
		default:
			break;
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
		// overridePendingTransition(R.anim.slide_in_right,
		// R.anim.slide_out_left);
	}

	@Override
	public void onRefresh(PullDownScrollView view) {
		getRemainderList();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getRemainderList();				
			}

		}, 500);
	}

	/**
	 * 获取提醒列表
	 */
	private void getRemainderList() {
		if(MyApplication.Doctor==null){
			return;
		}
		String serverUrl = MyApplication.ServerUrl
				+ "/doctor/reminder/get?uid=" + app.Doctor.getDoctorID()
				+ "&sessionkey=" + app.Doctor.getSessionKey();

		HttpUtils.doGetAsyn(serverUrl, new CallBack() {
			@Override
			public void onRequestComplete(String result) {
				ArrayList<ReminderItem> list = jUtils.getReminderList(result);
				if (list != null) {
					mList = list;
					mHandler.sendEmptyMessage(1);
				} else {
					mHandler.sendEmptyMessage(2);
				}
			}
		});
	}

	
	private ArrayList<ReminderItem> filiter(String date){
		if(mList==null){
			return null;
		}
		ArrayList<ReminderItem> list  = new ArrayList<>();
		
		for(ReminderItem item:mList){
			if(item.getStartDate().startsWith(date)){
				list.add(item);
			}
		}
		
		return list;
	}
	
	
	private void saveRemindList(ArrayList<ReminderItem> list){
		if(list==null){
			return;
		}
		DataSupport.deleteAll(ReminderItem.class);
		for(ReminderItem item:list){
			item.save();
		}
	}
}
