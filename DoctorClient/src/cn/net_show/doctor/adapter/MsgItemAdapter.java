/**  
 * @Title: MsgItemAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @author 王帅
 * @date 2015年3月2日 下午5:19:45  
 */
package cn.net_show.doctor.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import mark.utils.BitmapTool;
import mark.utils.DateFormat;
import mark.utils.HttpUtils;
import mark.utils.SimpleUtils;
import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.MsgItem;
import cn.net_show.doctor.service.PlayVoiceService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 消息显示adapter
 * @ClassName: MsgItemAdapter
 * @author 王帅
 * @date 2015年3月2日 下午5:19:45
 */
@SuppressLint("RtlHardcoded")
public class MsgItemAdapter extends BaseAdapter {
	private Context mContext;
	private List<MsgItem> mList;
	private DisplayImageOptions options;
	private ImageLoader imgLoader;
	private Bitmap bmp_error;
	private Handler mHandler;
	private int voiceImageId;
	private MyApplication app;
	private HashMap<String, Bitmap> imageCache;
	//private ExecutorService pool;
	/**
	 * @param mContext
	 * @param mList
	 */
	public MsgItemAdapter(Context mContext, List<MsgItem> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
		imageCache = MyApplication.getImageCache();
		app =(MyApplication) mContext.getApplicationContext();
		mHandler = new Handler(mContext.getMainLooper());
		bmp_error =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pic_error);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.default2)
			.showImageForEmptyUri(R.drawable.default2)
			.showImageOnFail(R.drawable.default2)
			.cacheInMemory(true)
			.cacheOnDisk(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(20))
			.build();
		imgLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.msg_row, null);
			holder = new ViewHolder();
			holder.time = (TextView) convertView.findViewById(R.id.txt_msg_time);
			holder.localHeader = (ImageButton) convertView.findViewById(R.id.headImage_myself);
			holder.otherHeader = (ImageButton) convertView.findViewById(R.id.headImage_other);
			holder.image = (ImageView) convertView.findViewById(R.id.imgView_picMsg);
			holder.msgContainer = (LinearLayout) convertView.findViewById(R.id.msg_container);
			holder.txtMsg = (TextView) convertView.findViewById(R.id.txt_msg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				convertView = View.inflate(mContext, R.layout.msg_row, null);
				holder = new ViewHolder();
				holder.time = (TextView) convertView.findViewById(R.id.txt_msg_time);
				holder.localHeader = (ImageButton) convertView.findViewById(R.id.headImage_myself);
				holder.otherHeader = (ImageButton) convertView.findViewById(R.id.headImage_other);
				holder.image = (ImageView) convertView.findViewById(R.id.imgView_picMsg);
				holder.msgContainer = (LinearLayout) convertView.findViewById(R.id.msg_container);
				holder.txtMsg = (TextView) convertView.findViewById(R.id.txt_msg);
				convertView.setTag(holder);
			}
		}

		final MsgItem item = mList.get(position);
		holder.image.setTag(item.getContent());
		holder.time.setText(DateFormat.toDateString(item.getTime(),
				"MM-dd HH:mm"));
		Log.i("time msg", item.getTime() + "+" + item.getContent());
		if (item.getIsSend() == MsgItem.SEND) {
			voiceImageId = R.drawable.selector_voice_right;
			//holder.msgContainer.setBackgroundResource();
			holder.txtMsg.setBackgroundResource(R.drawable.message_right);
			holder.msgContainer.setGravity(Gravity.RIGHT);
			holder.localHeader.setVisibility(View.VISIBLE);
			holder.otherHeader.setVisibility(View.INVISIBLE);
			imgLoader.displayImage(MyApplication.ServerUrl+app.Doctor.getAvatar(), holder.localHeader, options);
		} else {
			//holder.msgContainer.setBackgroundResource(R.drawable.message_left);
			holder.txtMsg.setBackgroundResource(R.drawable.message_left);
			voiceImageId = R.drawable.selector_voice_left;
			holder.msgContainer.setGravity(Gravity.LEFT);
			holder.otherHeader.setVisibility(View.VISIBLE);
			holder.localHeader.setVisibility(View.INVISIBLE);
			
		}
		if (item.getType() == MsgItem.TYPE_IMAGE) {
			holder.txtMsg.setVisibility(View.GONE);
			holder.image.setVisibility(View.VISIBLE);
			//loadImage(holder.image, item.getContent(),item.getIsSend()==MsgItem.SEND);
			loadImage(holder.image, item);
			holder.image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					viewImage(item);
				}
			});
		} else if (item.getType() == MsgItem.TYPE_VOICE) {
			holder.image.setVisibility(View.VISIBLE);
			//holder.image.setImageResource(R.drawable.laba);
			loadVoice(holder.image, item);
			holder.txtMsg.setVisibility(View.GONE);
			holder.image.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					playVoice(item);
				}
			});
		} else {
			holder.txtMsg.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.txtMsg.setText(item.getContent());
		}
		return convertView;
	}

	class ViewHolder {
		TextView time; // 消息时间
		LinearLayout msgContainer; //
		TextView txtMsg;
		ImageButton localHeader;
		ImageButton otherHeader;
		ImageView image;
	}

	public void viewImage(MsgItem item) {
		String path = item.getContent();
		String uri ="file://";
		if(item.getIsSend()==MsgItem.SEND){
			uri += path;
		}else{
			final File dir = mContext.getExternalCacheDir();
			uri += new File(dir,SimpleUtils.getFileNameFromPath(path)).getAbsolutePath();
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(uri), "image/*");
		mContext.startActivity(i);
	}

	// 图片异步加载
	private void loadImage(final ImageView imgbtn,final String path,boolean isSend) {
		if(isSend){
			imgLoader.displayImage(Uri.parse("file://"+path).toString(), imgbtn,options);
		}else{
			Log.e("imageUrl",MyApplication.ServerUrl+path);
			imgLoader.displayImage(MyApplication.ServerUrl+path,imgbtn,options);
		}
	}

	private void playVoice(MsgItem item) {
		String path = item.getContent();
		if(item.getIsSend()==MsgItem.RECEIVE){
			final File dir = mContext.getExternalCacheDir();
			if(dir==null){
				return;
			}
			String fileName = SimpleUtils.getFileNameFromPath(path);
			File amr =  new File(dir,fileName);
			if(amr.exists()){
				path =amr.getAbsolutePath();
			}else{
				return;
			}
		}
		Intent i = new Intent(mContext, PlayVoiceService.class);
		i.putExtra("PATH", path);
		mContext.startService(i);
	}
	
	private void loadImage(final ImageView imgbtn,MsgItem item){
		Bitmap cacheBmp = null;
		final String path = item.getContent();
		
		cacheBmp = imageCache.get(path);
		if(cacheBmp!=null){
			imgbtn.setImageBitmap(cacheBmp);
			return;
		}	
		File file  = null;	
		imgbtn.setTag(path);
		if(item.getIsSend()==MsgItem.SEND){		
//			loadImage(imgbtn,path,true);
			file = new File(item.getContent());
			if(file.exists()){
				cacheBmp = BitmapTool.getFastBitmap(path);	
				imageCache.put(path, cacheBmp);	
				imgbtn.setImageBitmap(cacheBmp);
				//imgbtn.setImageURI(Uri.parse("file://"+path));
			}
		}else{
//			loadImage(imgbtn,MyApplication.ServerUrl+path,false);	
			final File dir = mContext.getExternalCacheDir();
			if(dir==null){
				imgbtn.setImageBitmap(bmp_error);
				return;
			}
			final String fileName = SimpleUtils.getFileNameFromPath(path);
			file = new File(dir,fileName);
			if(file.exists()){
				cacheBmp = BitmapTool.getFastBitmap(file.getAbsolutePath());	
				imageCache.put(path, cacheBmp);	
				imgbtn.setImageBitmap(cacheBmp);
//				imgbtn.setImageBitmap(BitmapTool.getFastBitmap());
				return;
			}
			imgbtn.setImageResource(R.drawable.ic_stub);
			new Thread(){
				public void run(){
					Bitmap bmp = null;
					if(HttpUtils.download(MyApplication.ServerUrl+path,
							fileName, dir)){
						bmp = BitmapTool.getFastBitmap(new File(dir,fileName).getAbsolutePath());
						imageCache.put(path, bmp);
					}else{
						bmp = bmp_error;
					}
					final Bitmap img = bmp; 
					mHandler.post(new Runnable() {						
						@Override
						public void run() {
							if(imgbtn.getTag().equals(path)){
								imgbtn.setImageBitmap(img);
							}
						}
					});
				}
			}.start();
		}
	}
	/**
	 * @Title: loadVoice 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param imgbtn
	 * @param item   
	 * @return void
	 */
	private void loadVoice(final ImageView imgbtn,MsgItem item){
		File file  = null;
		final String path = item.getContent();
		imgbtn.setTag(path);
		if(item.getIsSend()==MsgItem.SEND){
			file = new File(item.getContent());
			if(file.exists()){
				imgbtn.setImageResource(voiceImageId);
			}
		}else{
			final File dir = mContext.getExternalCacheDir();
			if(dir==null){
				imgbtn.setImageBitmap(bmp_error);
				return;
			}
			final String fileName = SimpleUtils.getFileNameFromPath(path);
			file = new File(dir,fileName);
			if(file.exists()){
				imgbtn.setImageResource(R.drawable.laba);
				return;
			}
			imgbtn.setImageResource(R.drawable.ic_stub);
			new Thread(){
				public void run(){
					int bmpid =0;
					if(HttpUtils.download(MyApplication.ServerUrl+path,
							fileName, dir)){
						bmpid = R.drawable.laba; 
					}else{						
						bmpid = R.drawable.pic_error; 
					}
					final int resId = bmpid;
					mHandler.post(new Runnable() {						
						@Override
						public void run() {
							if(imgbtn.getTag().equals(path)){
								imgbtn.setImageResource(resId);
							}
						}
					});
				}
			}.start();
		}
	}
	
}
