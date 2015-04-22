/**  
 * @Title: MyGridView.java 
 * @Package cn.net_show.doctor.widget 
 * @author 王帅
 * @date 2015年3月4日 下午5:51:25  
 */ 
package mark.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/** 
 * @ClassName: MyGridView 
 * @author 王帅
 * @date 2015年3月4日 下午5:51:25  
 */
public class MyGridView extends GridView { 

    public MyGridView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public MyGridView(Context context) { 
        super(context); 
    } 

    public MyGridView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
    //将控件的高度设置为自动为最大值
    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
} 