package cn.net_show.doctor.widget;

import java.util.ArrayList;
import mark.widget.MoveableChart;
import mark.widget.MoveableChart.Data;
import cn.net_show.doctor.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomerChart extends LinearLayout {
	private Context mContext;
	private TextView title;
	private MoveableChart chart;
	
	
	@SuppressLint("NewApi")
	public CustomerChart(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		init(context);
	}

	@SuppressLint("NewApi")
	public CustomerChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CustomerChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CustomerChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public void init(Context context){
		mContext = context;
		inflate(mContext, R.layout.chartview, this);
		title = (TextView) findViewById(R.id.chart_title);
		chart = (MoveableChart) findViewById(R.id.chart);
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//RoundedBitmapDrawable bmp =RoundedBitmapDrawable.
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	public void setChartConfig(Config config){
		
		chart.setMaxYValue(config.maxYValue);
		chart.setMinYValue(config.minYValue);
		chart.setData(config.datas);
		chart.setConfigEnd();
	}
	
	public static class Config{
		public float minYValue=100, maxYValue=200;
		public ArrayList<Data> datas;
	}
	
	public void setTitle(String sTitle){
		title.setText(sTitle);
	}
	
	
}
