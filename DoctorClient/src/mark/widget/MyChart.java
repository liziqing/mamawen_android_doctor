/**  
 * @Title: MyChart.java 
 * @Package cn.net_show.doctor.widget 
 * @author 王帅
 * @date 2015年2月25日 下午3:39:49  
 */ 
package mark.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/** 
 * @ClassName: MyChart 
 * @Description: 趋势图
 * @author 王帅
 * @date 2015年2月25日 下午3:39:49  
 */
@SuppressLint("ClickableViewAccessibility")
public class MyChart extends View {
	private Context mContext;
	private int w=0;	//控件宽
	private int h = 0;	//控件高
	private DisplayMetrics dm;	//屏幕尺寸数据
	/**
	 * 内边距
	 */
	private float padding_top=40,padding_bottom=40,padding_left=60,padding_right=40;
	/**
	 * 图表的上下左右坐标
	 */
	private float chartTop=0,chartBottom=h,chartLeft=0,chartRight=w;
	
	/**
	 * 坐标轴字体大小
	 */
	private float label_text_sizeX=14,	label_text_sizeY=14;//坐标轴刻度字体大小
	
	/**
	 * 网格线条颜色
	 */
	private int grid_line_color;		//网格线条颜色
	
	/**
	 * 数据线颜色
	 */
	private int date_line_color;		//数据线颜色
	/**
	 * 数据区域颜色
	 */
	private int date_area_color;	
	/**
	 * 图表背景颜色
	 */
	private int chart_background_color;		//数据包尾区域颜色
	/**
	 * 控件背景色    坐标刻度标签的背景色
	 */
	private int backColor;		//背景色
	/**
	 * 控件刻度标签的字体颜色
	 */
	private int label_text_colorX=Color.BLACK,label_text_colorY=Color.BLACK;
	
	private float VSP,HSP;
	
	private String[] x_labels;//横坐标刻度
	private float[] y_labels; //纵坐标刻度
	private PointF[] data;	  //点坐标
	
	/**
	 * 设置刻度标签字体大小  
	 * @Title: setLabelTextSize 
	 * @Description:  设置刻度标签字体大小  X轴和Y轴同等大小
	 * @param size   
	 * @return void
	 */
	public  void setLabelTextSize(float size){
		label_text_sizeX = size;
		label_text_sizeY = size;
	}
	/**
	 * 设置X轴坐标刻度标签字体大小
	 * @Title: setLabelTextSizeX 
	 * @Description: 设置X轴坐标刻度标签字体大小
	 * @param sizeX   
	 * @return void
	 */
	public void setLabelTextSizeX(float sizeX){
		label_text_sizeX = sizeX;
	}
	/**
	 * 设置X轴坐标刻度标签字体大小
	 * @Title: setLabelTextSizeY 
	 * @Description: 设置X轴坐标刻度标签字体大小
	 * @param sizeX   
	 * @return void
	 */
	public void setLabelTextSizeY(float sizeY){
		label_text_sizeY = sizeY;
	}
	public MyChart(Context context) {
		super(context);	
		mContext = getContext();
		init();
	}
	
	@SuppressLint("NewApi")
	public MyChart(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		mContext = getContext();
		init();
	}
	
	public MyChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = getContext();
		init();
	}
	
	public MyChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = getContext();
		init();
		
	}
	
	private void init(){
		dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		
		//初始化控件背景色
		backColor= Color.parseColor("#22FFFFFF");
		//初始化标签字体颜色
		label_text_colorX = Color.BLACK;
		label_text_colorY = Color.BLACK;
		//初始化表格线颜色
		grid_line_color = Color.parseColor("#aaaaaa");
		//初始化图表背景色
		chart_background_color= Color.parseColor("#22FFFFFF");
		//初始化
		date_line_color = Color.RED;
		date_area_color = Color.RED;
		//测试数据  标签
		x_labels = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
		y_labels = new float[]{10,11,12,13,14,15,16,17,18,19};
		setClickable(true);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		w = getWidth();
		h = getHeight();
		chartLeft = padding_left;
		chartTop = padding_top;
		chartRight = w-padding_right;
		chartBottom = h -padding_bottom;
		drawBackground(canvas);
		drawGrid(canvas);
		dataToPoint();
		drawDataLine(canvas);
		drawLabelX(canvas);
		drawLabelY(canvas);
	}
	/**
	 * 绘制表格线
	 * @Title: drawGrid 
	 * @Description: 绘制表格线
	 * @param canvas   
	 * @return void
	 */
	private void drawGrid(Canvas canvas) {
		// 绘制表格线
		Paint paint = new Paint();
		paint.setColor(grid_line_color);
		paint.setStrokeWidth(1);
		// 纵向表格线
		HSP = (chartRight-chartLeft)/(x_labels.length-1);
		for (int i = 0; i < x_labels.length; i++) {
			canvas.drawLine( HSP*i + padding_left, chartTop, HSP * i + padding_left,chartBottom, paint);
		}
		// 横向表格线
		VSP = (chartBottom-chartTop)/(y_labels.length-1);
		for (int i = 0; i < y_labels.length; i++) {
			canvas.drawLine(chartLeft, padding_top+VSP*i,chartRight, padding_top+VSP*i, paint);
		}
	}
	/**
	 * @Title: drawBackground 
	 * @param canvas   
	 * @return void
	 */
	private void drawBackground(Canvas canvas){
		//绘制控件背景
		canvas.drawColor(backColor);
		//绘制图表区域背景
		Paint paint = new Paint();
		paint.setColor(chart_background_color);
		canvas.drawRect(chartLeft, chartTop, chartRight, chartBottom, paint);		
	}
	/**
	 * 绘制X轴坐标标签
	 * @Title: drawLabelX 
	 * @Description: 绘制X轴坐标标签
	 * @param canvas   
	 * @return void
	 */
	private void drawLabelX(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(label_text_colorX);
		paint.setAntiAlias(true);
		paint.setTextSize(label_text_sizeX);
		paint.setTextAlign(Paint.Align.CENTER);
		float V = h-padding_bottom/2;
		if(x_labels!=null){
			HSP = (chartRight-chartLeft)/(x_labels.length-1);		
			for(int i=0;i<x_labels.length;i++){
				canvas.drawText(x_labels[i],padding_left + HSP*i, V, paint);
			}
		}
	}
	/**
	 * 绘制Y轴坐标标签
	 * @Title: drawLabelY 
	 * @Description: 绘制Y轴坐标标签 
	 * @param canvas   
	 * @return void
	 */
	private void drawLabelY(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(label_text_colorY);
		paint.setAntiAlias(true);
		paint.setTextSize(label_text_sizeY);
		paint.setTextAlign(Paint.Align.CENTER);
		if(x_labels!=null){
			VSP = (chartBottom-chartTop)/(y_labels.length-1);		
			for(int i=0;i<y_labels.length;i++){
				canvas.drawText(y_labels[i]+"", padding_left/2, chartBottom-i*VSP, paint);
			}
		}
	}
	/**
	 * 绘制数据线
	 * @Title: drawDataLine 
	 * @Description: 绘制数据线 
	 * @param canvas   
	 * @return void
	 */
	private void drawDataLine(Canvas canvas){
		if(data!=null){
			//绘制曲线
			Path path = new Path();
			path.moveTo(chartLeft, chartBottom);
			for(int i=0;i<data.length;i++){
				path.lineTo(data[i].x, data[i].y);
			}
			path.lineTo(chartRight, chartBottom);
			Paint paint = new Paint();
			paint.setColor(date_line_color);		
			paint.setStrokeWidth(3);
			paint.setAlpha(100);
			paint.setAntiAlias(true);
			//path.close();
			paint.setStyle(Style.STROKE);
			//paint.setStyle(Style.FILL_AND_STROKE);
			canvas.drawPath(path, paint);
			
			//绘制区域图
			paint = new Paint();
			paint.setColor(date_area_color);
			paint.setAlpha(50);
			path.close();
			canvas.drawPath(path, paint);		
		}
	}
	/**
	 * 测试数据
	 * @Title: dataToPoint 
	 * @Description: TODO(这里用一句话描述这个方法的作用)    
	 * @return void
	 */
	private void dataToPoint(){
		if(data==null){
			data = new PointF[7];
		
			PointF p = null;
			for(int i=0;i<7;i++){
				p = new PointF();
				p.x = padding_left+HSP*i;
				p.y = (float) (chartBottom-VSP*Math.random()*9); 
				data[i]=p;
			}
		}
	}
	
	public void setData(){
		dataToPoint();
		invalidate();
	}
	//仅供测试使用
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if(event.getAction()==MotionEvent.ACTION_DOWN){
//			setData();
//		}
//		return super.onTouchEvent(event);
//	}
	//仅供测试使用
		private float down_x;
		//private float down_y;
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				down_x=event.getX();
				//down_y=event.getY();
				break;
			case MotionEvent.ACTION_UP:
				if(mSlideListener!=null){
					if(x-down_x<-50){
						mSlideListener.onSlide(true);
						//Toast.makeText(getContext(), "后一页", Toast.LENGTH_SHORT).show();
					}else if(x-down_x>50){
						mSlideListener.onSlide(false);
						//Toast.makeText(getContext(), "前一页", Toast.LENGTH_SHORT).show();				
					}
				}
				break;
			}
			return super.onTouchEvent(event);
		}
		
		public interface OnSlideListener{
			/**
			 * 
			 * @param isNext true (next) or false (previous)
			 */
			public void onSlide(boolean isNext);
		};
			
		private OnSlideListener mSlideListener;
		
		public void setOnSlideListenr(OnSlideListener listener){
			this.mSlideListener = listener;
		}
	
}
