/**  
 * @Title: AutoFilperView.java 
 * @Package cn.net_show.doctor.widget 
 * @author 王帅
 * @date 2015年3月4日 下午2:26:30  
 */ 
package mark.widget;
import java.util.ArrayList;  
import java.util.List;  
import java.util.Timer;  
import java.util.TimerTask;  

import mark.utils.SimpleUtils;
import cn.net_show.doctor.R;
import android.annotation.SuppressLint;
import android.content.Context;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
import android.os.Handler;  
import android.util.AttributeSet;  
import android.view.Gravity;  
import android.view.MotionEvent;  
import android.view.View;  
import android.view.ViewConfiguration;  
import android.widget.FrameLayout;  
import android.widget.ImageView;  
import android.widget.ImageView.ScaleType;  
import android.widget.LinearLayout;  
import android.widget.ViewFlipper;  
  
/** 
 * 自定播放图片View 
 * @author lixiaoqiang 
 *  
 * CSDN博客：http://blog.csdn.net/dawanganban 
 * 
 */  
public class AutoViewFilpper extends FrameLayout{  
    private static final int SLOP_DELAY_TIME = 5000; //滑动等待时间，单位ms  
    private ViewFlipper mViewFlipper;       //滑动的视图  
    private View mPointBar;                 //指示点显示条  
    private int mItemCount;                 //条目数  
    private int mCurrentItem;               //当前的条目  
    private int mTouchSlop;                 //有效最短滑动距离  
    private Context context;  
    private List<ImageView> points = new ArrayList<ImageView>();  
      
    private TimerTask mTimerTask;  
      
    private static final int HANDLER_SLOP_LEFT = 0x0001;  
    @SuppressLint("HandlerLeak")
	private Handler hander = new Handler(){  
        public void handleMessage(android.os.Message msg) {  
            switch (msg.what) {  
            case HANDLER_SLOP_LEFT:  
                slopToLeft();  
                break;  
            }  
        };  
    };  
    
    @SuppressWarnings("deprecation")
	public AutoViewFilpper(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        this.context = context;  
          
        mTouchSlop = ViewConfiguration.getTouchSlop();  
          
        addChild(context);  
          
        startAutoPlay();  
    }  
      
    /** 
     * 停止自动播放 
     */  
    public void stopAutoPlay(){  
        if(mTimerTask == null) return;  
        mTimerTask.cancel();  
        mTimerTask = null;  
    }  
      
    /** 
     * 开始自动播放 
     */  
    public void startAutoPlay(){  
        if(mTimerTask != null) return;  
        mTimerTask = new TimerTask() {  
            @Override  
            public void run() {  
                hander.sendEmptyMessage(HANDLER_SLOP_LEFT);  
            }  
        };  
        new Timer().scheduleAtFixedRate(mTimerTask, SLOP_DELAY_TIME, SLOP_DELAY_TIME);  
    }  
      
    /** 
     * 添加子视图 
     * @param context 
     */  
    private void addChild(Context context){  
          
        mViewFlipper = getViewFlipper(context);  
        this.addView(mViewFlipper);  
          
        mPointBar = getPointBar(context);  
        this.addView(mPointBar);  
    }  
      
    /** 
     * 获取ViewFlipper 
     * @param context 
     * @return 
     */  
    private ViewFlipper getViewFlipper(Context context){  
        ViewFlipper viewFlipper = new ViewFlipper(context);  
        addImageViews(context, viewFlipper);  
        return viewFlipper;  
    }  
      
    /** 
     * 获取指示点显示条 
     * @param context 
     * @return 
     */  
    private View getPointBar(Context context){  
        LinearLayout.LayoutParams pointBarParams = new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);  
        pointBarParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;  
        LinearLayout pointBar = new LinearLayout(context);  
          
        pointBar.setOrientation(LinearLayout.HORIZONTAL);  
        pointBar.setLayoutParams(pointBarParams);  
        pointBar.setGravity(Gravity.RIGHT | Gravity.BOTTOM);  
          
        addPoints(context, pointBar);  
        return pointBar;  
    }  
      
    /** 
     * 添加小圆点 
     * @param context 
     * @param pointBar 
     */  
    private void addPoints(Context context, LinearLayout pointBar){  
        LinearLayout.LayoutParams pointViewParams = new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
        pointViewParams.setMargins(SimpleUtils.dp2px(context, 8), 0,  
        		SimpleUtils.dp2px(context, 8), SimpleUtils.dp2px(context, 8));  
          
        ImageView pointView=null;  
        for(int i = 0; i < mItemCount; i++){  
            pointView = new ImageView(context);  
            pointView.setScaleType(ScaleType.CENTER_INSIDE);  
            pointView.setLayoutParams(pointViewParams);  
            points.add(pointView);  
            pointBar.addView(pointView);  
        }  
        setPointColorByCurrentItem();  
    }  
      
    /** 
     * 根据当前选中项来设置圆点 
     */  
    private void setPointColorByCurrentItem(){  
        mCurrentItem = (Integer)mViewFlipper.getCurrentView().getTag();  
        Bitmap grayPointBitmap = getGrayPointBitmap(context);  
        Bitmap lightPointBitmap = getLightPointBitmap(context);  
        ImageView imageView;  
        for(int i = 0; i < points.size(); i++){  
            imageView = points.get(i);  
            if(mCurrentItem == i){  
                imageView.setImageBitmap(lightPointBitmap);  
            }else{  
                imageView.setImageBitmap(grayPointBitmap);  
            }  
  
        }  
    }  
      
    /** 
     * 添加图片资源 
     * @param context 
     * @param viewFlipper 
     */  
    private void addImageViews(Context context, ViewFlipper viewFlipper){  
        if(viewFlipper == null) return;  
        List<Bitmap> bitmaps = getBitmaps();  
          
        if(bitmaps == null) return;  
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);  
          
        ImageView imageView;  
        mItemCount = bitmaps.size();  
        for(int i = 0; i < mItemCount; i++){  
            imageView = new ImageView(context);  
            imageView.setImageBitmap(bitmaps.get(i));  
            imageView.setScaleType(ScaleType.FIT_XY);//CENTER_CROP);  
            imageView.setLayoutParams(imageViewParams);  
            imageView.setTag(i);  
            viewFlipper.addView(imageView);  
        }     
    }  
      
    /** 
     * 获取图片资源 
     * @return 
     */  
    private List<Bitmap> getBitmaps(){  
        //TODO 从网络获取图片  
        List<Bitmap> bitmaps = new ArrayList<Bitmap>();   
          
        bitmaps.add(BitmapFactory.decodeResource(  
                getResources(), R.drawable.ad1));  
        bitmaps.add(BitmapFactory.decodeResource(  
                getResources(), R.drawable.ad2));  
        bitmaps.add(BitmapFactory.decodeResource(  
                getResources(), R.drawable.ad3));  
          
        return bitmaps;  
    }  
      
    /** 
     * 获取选择圆点图片 
     * @param context 
     * @return 
     */  
    private Bitmap getLightPointBitmap(Context context){  
        return BitmapFactory.decodeResource(  
                getResources(), R.drawable.point_selected);  
    }  
      
    /** 
     * 获取灰色圆点图片 
     * @param context 
     * @return 
     */  
    private Bitmap getGrayPointBitmap(Context context){  
        return BitmapFactory.decodeResource(  
                getResources(), R.drawable.point_unselected);  
    }  
      
      
    private float mDownX;  
    @SuppressLint("ClickableViewAccessibility")
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        float eventX = event.getX();  
          
        switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            mDownX = eventX;  
            break;  
        case MotionEvent.ACTION_UP:  
              
            float gap = eventX - mDownX;  
            if(Math.abs(gap) > mTouchSlop){  
                if(gap > 0){  
                    //向右滑动  
                    slopToRight();  
                }else{  
                    //向左滑动  
                    slopToLeft();  
                }  
            }  
            break;  
        }  
          
        return true;  
    }  
      
    /** 
     * 向右滑动 
     */  
    private void slopToRight(){  
        mViewFlipper.setInAnimation(context, R.anim.slide_in_left);  
        mViewFlipper.setOutAnimation(context, R.anim.slide_out_right);  
        mViewFlipper.showPrevious();  
        setPointColorByCurrentItem();  
    }  
      
    /** 
     * 向左滑动 
     */  
    private void slopToLeft(){  
        mViewFlipper.setInAnimation(context, R.anim.slide_in_right);  
        mViewFlipper.setOutAnimation(context, R.anim.slide_out_left);  
        mViewFlipper.showNext();  
        setPointColorByCurrentItem();  
    }  
      
    public OnSlopTouchListener mOnSlopTouchListener;  
      
    /** 
     * 监听滑动等事件 
     * @author Administrator 
     * 
     */  
    interface OnSlopTouchListener{  
          
        /** 
         * touch事件响应 
         */  
        public void onTouchedView();  
    }  
      
    /** 
     * 设置滑动等事件的监听 
     * @param onSlopTouchListener 
     */  
    public void setOnSlopTouchListener(OnSlopTouchListener onSlopTouchListener){  
        this.mOnSlopTouchListener = onSlopTouchListener;  
    }  
    
    
}