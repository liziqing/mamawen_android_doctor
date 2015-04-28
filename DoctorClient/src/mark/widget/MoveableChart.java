package mark.widget;

import java.util.ArrayList;

import mark.utils.Logger;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MoveableChart extends View {

	private int padding_left = 0, padding_top = 0, padding_right = 0,
			padding_bottom = 60;
	private Context mContext;
	private Data[] datas;
	private int w = 0; // 控件宽
	private int h = 0; // 控件高
	private DisplayMetrics dm; // 屏幕尺寸数据
	private Bitmap bitmap; // 图表图像
	private int ScreenHeight, ScreenWidth; // 屏幕尺寸
	private int bmpWidth, bmpHeight; // 图表图像的实际尺寸
	private boolean isStored = false; // 图表图像是否已存在 不存在重绘
	private int destX, destY; // 移动的目标位置
	private Paint mPaint; // 默认画笔对象
	private float lastX, lastY; // 手指最后移动的位置
	private int VSP, HSP; // 垂直间隔vsp 水平间隔hsp
	private int VspNum, HspNum; // 垂直、水平方向图表的间隔数目
	private String[] x_labels; // 横坐标刻度
	private float[] y_labels; // 纵坐标刻度
	private PointF[] data; // 点坐标
	private int miniHeight, miniWidth; // 图表所要求的最小高度
	private float minXValue, maxXValue, minYValue, maxYValue;
	// private int
	/**
	 * 图表的上下左右坐标
	 */
	private float chartTop = 0, chartBottom = h, chartLeft = 0, chartRight = w;

	/**
	 * 坐标轴字体大小
	 */
	private float label_text_sizeX = 28, label_text_sizeY = 28;// 坐标轴刻度字体大小

	/**
	 * 网格线条颜色
	 */
	private int grid_line_color; // 网格线条颜色

	/**
	 * 数据线颜色
	 */
	private int data_line_color; // 数据线颜色
	/**
	 * 数据区域颜色
	 */
	private int data_area_color;
	/**
	 * 图表背景颜色
	 */
	private int chart_background_color; // 数据包尾区域颜色
	/**
	 * 控件背景色 坐标刻度标签的背景色
	 */
	private int backColor; // 背景色
	/**
	 * 控件刻度标签的字体颜色
	 */
	private int label_text_colorX = Color.BLACK,label_text_colorY = Color.BLACK;

	public MoveableChart(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MoveableChart(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public MoveableChart(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		dm = new DisplayMetrics();
		mPaint = new Paint();
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);

		ScreenHeight = dm.heightPixels;
		ScreenWidth = dm.widthPixels;

		// 初始化控件背景色
		// backColor = Color.parseColor("#22FFFFFF");
		backColor = Color.TRANSPARENT;
		// 初始化标签字体颜色
		label_text_colorX = Color.WHITE;
		label_text_colorY = Color.WHITE;
		// 初始化表格线颜色
		// grid_line_color = Color.parseColor("#aaaaaa");
		grid_line_color = Color.WHITE;

		// 初始化图表背景色
		// chart_background_color = Color.parseColor("#22FFFFFF");
		chart_background_color = Color.TRANSPARENT;
		// 初始化
		data_line_color = Color.WHITE;
		data_area_color = Color.parseColor("#55FFFFFF");
		VspNum = 5;
		HspNum = 41;
		VSP = 100;
		HSP = 50;
		miniHeight = VspNum * VSP + padding_bottom + padding_top;
		miniWidth = HspNum * HSP + padding_left + padding_right;
		setMinimumHeight(miniHeight);
		setClickable(true);
		minXValue = 0;
		minYValue = 0;
		maxXValue = 40;
		maxYValue = 40;
	}

	private void getXLabels() {

	}

	private void getYLabels() {

	}

	/**
	 * @Title: drawBackground
	 * @param canvas
	 * @return void
	 */
	private void drawBackground(Canvas canvas) {
		// 绘制控件背景
		canvas.drawColor(backColor);
		if (chart_background_color == Color.TRANSPARENT) {
			return;
		}
		// 绘制图表区域背景
		mPaint.setColor(chart_background_color);
		canvas.drawRect(chartLeft, chartTop, chartRight, chartBottom, mPaint);
	}

	/**
	 * 绘制水平线 水平坐标
	 * 
	 * @param paint
	 * @param canvas
	 * @param length
	 */
	private void drawXLines(Paint paint, Canvas canvas, int length) {
		Log.e("------", "eeeeee XXXXXXXXXXXXXXXX");
		int ynum = 0;
		for (int i = 0; i <= VspNum; i++) {
			ynum = padding_top + VSP * i;
			if (i == VspNum) {
				paint.setStrokeWidth(4);
			}
			canvas.drawLine(chartLeft, ynum, chartRight, ynum, paint);
		}
		// 绘制水平坐标
		paint.setColor(label_text_colorY);
		paint.setAntiAlias(true);
		paint.setTextSize(label_text_sizeY);
		paint.setTextAlign(Paint.Align.CENTER);
		int fontHeight = getFontHeight(mPaint);
		for (int i = 1; i < HspNum; i++) {
			canvas.drawText(i + "", padding_left + i * HSP + 5, ynum+ fontHeight + 10, paint);
		}

	}

	/**
	 * 绘制垂直线
	 * 
	 * @param paint
	 * @param canvas
	 * @param length
	 */
	private void drawYLines(Paint paint, Canvas canvas, int length) {

		for (int i = 0; i <= HspNum + 1; i++) {
			canvas.drawLine(HSP * i + padding_left, chartTop, HSP * i
					+ padding_left, chartBottom, paint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		if (isStored) {

		} else {
			h = getHeight();
			w = getWidth();
			bmpWidth = ((miniWidth > w) ? miniWidth : w);
			bmpHeight = ((miniHeight > h) ? miniHeight : h);
			if (miniWidth < w) {
				HSP = (w - padding_left - padding_right) / HspNum;
			}
			// 图表区域的相对坐标
			chartLeft = padding_left;
			chartTop = padding_top;
			chartBottom = bmpHeight - padding_bottom;
			chartRight = bmpWidth - padding_right;

			drawBitmap(bmpWidth, bmpHeight);
			isStored = true;
		}
		canvas.drawBitmap(bitmap, -destX, 0, mPaint);
		super.onDraw(canvas);
	}

	private void drawBitmap(int width, int height) {
		if(bitmap!=null){
			bitmap.recycle();
		}
		mPaint = null;
		mPaint = new Paint();
		
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas bcanvas = new Canvas(bitmap);
		drawBackground(bcanvas);

		mPaint.setColor(grid_line_color);
		drawXLines(mPaint, bcanvas, (int) (height / VSP));
		bcanvas.save();
		// dataToPoint();
		drawDataLine(bcanvas);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_MOVE:
			int delatX = (int) (x - lastX);
			int dx = destX - delatX;
			if (dx > bmpWidth - w) {
				dx = bmpWidth - w;
			} else if (dx < 0) {
				dx = 0;
			}
			if (destX != dx) {
				destX = dx;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		lastX = x;
		return super.onTouchEvent(event);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int measuredHeight = measureHeight(heightMeasureSpec);

		int measuredWidth = measureWidth(widthMeasureSpec);

		super.onMeasure(
				MeasureSpec.makeMeasureSpec(measuredWidth,
						MeasureSpec.getMode(widthMeasureSpec)),
				MeasureSpec.makeMeasureSpec(measuredHeight,
						MeasureSpec.getMode(heightMeasureSpec)));
		// setMeasuredDimension(measuredHeight, measuredWidth);

	}

	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			// Calculate the ideal size of your control within this maximum
			// size.
			// If your control fills the available space return the outer bound.
			result = miniHeight;
		} else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that value.
			VSP = (specSize - padding_bottom - padding_top) / VspNum;
			result = specSize;
		} else {
			return miniHeight;
		}
		return result;
	}

	private int measureWidth(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.
		// MeasureSpec.AT_MOST 最大
		// MeasureSpec.EXACTLY 精确值
		// MeasureSpec.UNSPECIFIED 未指定 任意
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST) {
			// Calculate the ideal size of your control
			// within this maximum size.
			// If your control fills the available space
			// return the outer bound.
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			// If your control can fit within these bounds return that value.

			result = specSize;
		}

		return result;
	}

	private int getFontHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.ascent);
	}

	/**
	 * 绘制数据线
	 * 
	 * @Title: drawDataLine
	 * @Description: 绘制数据线
	 * @param canvas
	 * @return void
	 */
	private void drawDataLine(Canvas canvas) {
		dataToPoint(datas);
		if (data != null) {
			Paint paint = new Paint();
			// paint.setColor(Color.WHITE);
			paint.setStrokeWidth(3);
			paint.setAlpha(100);
			paint.setAntiAlias(true);
			// path.close();
			paint.setColor(data_line_color);
			paint.setStyle(Style.STROKE);

			Paint paint2 = new Paint();
			paint2.setColor(Color.WHITE);

			// 绘制曲线
			Path path = new Path();
			boolean isFirst = true;
			// path.moveTo(chartLeft, chartBottom);
			for (int i = 0; i < data.length; i++) {
				if(data[i]==null){
					continue;
				}
				Logger.e("x="+data[i].x+"  y="+data[i].y);
				if (isFirst) {
					path.moveTo(data[i].x, data[i].y);
					isFirst = false;
				} else {
					path.lineTo(data[i].x, data[i].y);
				}
				canvas.drawCircle(data[i].x, data[i].y, 5, paint2);
			}
			// path.lineTo(chartRight, chartBottom);

			if(data.length>1){
				//paint.setStyle(Style.STROKE);
				canvas.drawPath(path, paint);
			}

			// 绘制区域图
			// paint = new Paint();
			// paint.setColor(data_area_color);
			// paint.setAlpha(50);
			// path.close();
			// canvas.drawPath(path, paint);
		}
	}

	private float getYScale(float value) {
		
		float scale = (value - minYValue) / (maxYValue - minYValue);
		Logger.d("valeu="+value+" scale="+scale+" minYValue="+minYValue+" maxYValue="+maxYValue);
		return scale;
	}
	
	private void dataToPoint(Data[] datas) {
		if(datas==null){
			return;
		}
		if(datas.length<1){
			return;
		}
		PointF p = null;
		data = new PointF[datas.length];
		for (int i = 0; i < datas.length; i++) {
			
			int number = datas[i].number;
			if(number>HspNum){
				continue;
			}
			p = new PointF();
			p.x = padding_left + HSP * number;
			Logger.e("charBottom =" +chartBottom+"  chartTop="+chartTop);
			Logger.e(""+ (chartBottom - getYScale(datas[i].value)));
			p.y = chartBottom - getYScale(datas[i].value) * (chartBottom - chartTop);
			data[i] = p;
		}

	}

	public void setData(ArrayList<Data> mdatas) {
		if(mdatas==null||mdatas.size()<1){
			return;
		}
		this.datas = new Data[mdatas.size()];
		for(int i=0;i<datas.length;i++){
			datas[i]=mdatas.get(i);
		}
		
	}

	public int getPadding_left() {
		return padding_left;
	}

	public void setPadding_left(int padding_left) {
		this.padding_left = padding_left;
	}

	public int getPadding_top() {
		return padding_top;
	}

	public void setPadding_top(int padding_top) {
		this.padding_top = padding_top;
	}

	public int getPadding_right() {
		return padding_right;
	}

	public void setPadding_right(int padding_right) {
		this.padding_right = padding_right;
	}

	public int getPadding_bottom() {
		return padding_bottom;
	}

	public void setPadding_bottom(int padding_bottom) {
		this.padding_bottom = padding_bottom;
	}

	public int getHspNum() {
		return HspNum;
	}

	public void setHspNum(int hspNum) {
		HspNum = hspNum;
	}

	public float getMinXValue() {
		return minXValue;
	}

	public void setMinXValue(float minXValue) {
		this.minXValue = minXValue;
	}

	public float getMaxXValue() {
		return maxXValue;
	}

	public void setMaxXValue(float maxXValue) {
		this.maxXValue = maxXValue;
	}

	public float getMinYValue() {
		return minYValue;
	}

	/**
	 * Y方向最小值
	 * 
	 * @param minYValue
	 */
	public void setMinYValue(float minYValue) {
		this.minYValue = minYValue;
	}

	public float getMaxYValue() {
		return maxYValue;
	}

	/**
	 * Y方向最大值
	 * 
	 * @param maxYValue
	 */
	public void setMaxYValue(float maxYValue) {
		Logger.e("maxYValue="+maxYValue);
		this.maxYValue = maxYValue;
		Logger.e("new maxYValue="+this.maxYValue);
	}

	/**
	 * 设置X轴标签字体大小
	 * 
	 * @param label_text_sizeX
	 */
	public void setLabel_text_sizeX(float label_text_sizeX) {
		this.label_text_sizeX = label_text_sizeX;
	}

	/**
	 * 设置Y轴标签字体大小
	 * 
	 * @param label_text_sizeY
	 */
	public void setLabel_text_sizeY(float label_text_sizeY) {
		this.label_text_sizeY = label_text_sizeY;
	}

	/**
	 * 设置表格线的颜色
	 * 
	 * @param grid_line_color
	 */
	public void setGrid_line_color(int grid_line_color) {
		this.grid_line_color = grid_line_color;
	}

	/**
	 * 设置数据线的颜色
	 * 
	 * @param data_line_color
	 */
	public void setData_line_color(int data_line_color) {
		this.data_line_color = data_line_color;
	}

	/**
	 * 数据包裹区域的颜色
	 * 
	 * @param data_area_color
	 */
	public void setData_area_color(int data_area_color) {
		this.data_area_color = data_area_color;
	}

	/**
	 * 设置表格区域的背景色
	 * 
	 * @param chart_background_color
	 */
	public void setChart_background_color(int chart_background_color) {
		this.chart_background_color = chart_background_color;
	}

	/**
	 * 设置控件的背景色
	 * 
	 * @param backColor
	 */
	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	/**
	 * 设置X标签字体颜色
	 * 
	 * @param label_text_colorX
	 */
	public void setLabel_text_colorX(int label_text_colorX) {
		this.label_text_colorX = label_text_colorX;
	}

	/**
	 * 设置Y标签字体颜色
	 * 
	 * @param label_text_colorY
	 */
	public void setLabel_text_colorY(int label_text_colorY) {
		this.label_text_colorY = label_text_colorY;
	}

	public void setConfigEnd() {
		isStored = false;
		invalidate();
	}

	public static class Data{
		public int number;
		
		public float value;
	}
}
