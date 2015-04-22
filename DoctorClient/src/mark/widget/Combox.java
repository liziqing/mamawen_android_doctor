/**  
 * @Title: Combox.java 
 * @Package mark.widget 
 * @author 王帅
 * @date 2015年3月19日 下午1:10:47  
 */ 
package mark.widget;


import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

/** 
 * @ClassName: Combox 
 * @author 王帅
 * @date 2015年3月19日 下午1:10:47  
 */
public class Combox extends EditText {
	private String[] mList;
	private PopupWindow mDialog;
	private ListAdapter mAdapter;
	private AdapterView.OnItemClickListener mItemClickListener;
	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 * @param defStyleRes
	 */
	@SuppressLint("NewApi")
	public Combox(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context,attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public Combox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context,attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public Combox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	/**
	 * @param context
	 */
	public Combox(Context context) {
		super(context);
	}

	private void init(Context context,AttributeSet attrs){
		this.setClickable(true);
		//获取entries属性
		int entries = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "entries", -1);
		if(entries>0){
			try {
				mList = context.getResources().getStringArray(entries);
			} catch (NotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		this.requestFocus();
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
					&& (event.getX() < (getWidth()));
			if (isClean && getDialog()) {
				if(!mDialog.isShowing())
					mDialog.showAsDropDown(this);
				return true;
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
	public void setDataRes(List<String> list){
		if(list!=null){
			this.mList = list.toArray(new String[mList.length]);
		}else{
			mList = null;
		}
		mDialog = null;
		mAdapter = null;
	}
	
	public void setDataRes(String[] list){
		mList = list;
		mDialog = null;
		mAdapter = null;
	}
	
	public void setAdapter(ListAdapter adapter){
		mAdapter = adapter;
		mDialog = null;
		mList = null;
	}
	
	public void setOnItemClickListener(AdapterView.OnItemClickListener mItemClickListener){
		this.mItemClickListener = mItemClickListener;
	}
	
	private boolean getDialog(){
		
		if(mList == null || (mList.length<1 && mAdapter==null)){
			return false;
		}
		if(mDialog!=null){
			return true;
		}
		
		mDialog = new PopupWindow(getContext());
		mDialog.setWidth(Math.max(getWidth(), 500));
		mDialog.setHeight(LayoutParams.WRAP_CONTENT);
		ListView lv = new ListView(getContext());
		if(mAdapter==null){
			lv.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, mList));
			if(mItemClickListener == null){
				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
					public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
						Combox.this.setText(mList[position]);
						mDialog.dismiss();
					}
				});
			}else{
				lv.setOnItemClickListener(mItemClickListener);
			}
		}else{
			lv.setAdapter(mAdapter);
			if(mItemClickListener !=null){
				lv.setOnItemClickListener(mItemClickListener);
			}
		}
		
		lv.setBackgroundColor(Color.parseColor("#ffffff"));
		mDialog.setContentView(lv);
		mDialog.setOutsideTouchable(true);
		mDialog.setFocusable(true);
		return true;
		//lv.setBackgroundResource();
		//mDialog.setBackgroundDrawable();
	}
}
