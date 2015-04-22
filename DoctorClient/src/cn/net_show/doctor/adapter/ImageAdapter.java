/**  
 * @Title: ImageAdapter.java 
 * @Package cn.net_show.doctor.adapter 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月18日 下午5:13:32  
 */
package cn.net_show.doctor.adapter;

import java.io.File;
import java.util.HashMap;

import mark.utils.BitmapTool;
import mark.utils.HttpUtils;
import mark.utils.SimpleUtils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.MsgItem;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @ClassName: ImageAdapter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月18日 下午5:13:32
 * 
 */
public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	//private DisplayImageOptions options;
	//private ImageLoader imgLoader;
	private String[] mArray;
	private HashMap<String, Bitmap> imageCache;
	private Bitmap bmp_error;
	private Handler mHandler;

	public ImageAdapter(Context context, String[] mArray) {
		mContext = context;
		this.mArray = mArray;
		mHandler = new Handler(mContext.getMainLooper());
		imageCache = MyApplication.getImageCache();
		bmp_error = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.pic_error);
//		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.default2)
//				.showImageForEmptyUri(R.drawable.default2)
//				.showImageOnFail(R.drawable.default2).cacheInMemory(true)
//				.cacheOnDisk(true).considerExifParams(true)
//				.displayer(new RoundedBitmapDisplayer(20)).build();
//		imgLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (mArray == null) {
			return 0;
		}
		return mArray.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = new ImageView(mContext);
			((ImageView) convertView).setScaleType(ScaleType.CENTER);
		}
		//imgLoader.displayImage(MyApplication.ServerUrl + mArray[position],(ImageView) convertView, options);
		loadImage((ImageView) convertView,mArray[position]);
		convertView.setClickable(true);
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewImage(mArray[position]);
			}
		});
		return convertView;
	}

	private void loadImage(final ImageView imgbtn, final String path) {
		Bitmap cacheBmp = null;

		cacheBmp = imageCache.get(path);
		if (cacheBmp != null) {
			imgbtn.setImageBitmap(cacheBmp);
			return;
		}
		File file = null;
		imgbtn.setTag(path);
		// loadImage(imgbtn,MyApplication.ServerUrl+path,false);
		final File dir = mContext.getExternalCacheDir();
		if (dir == null) {
			imgbtn.setImageBitmap(bmp_error);
			return;
		}
		final String fileName = SimpleUtils.getFileNameFromPath(path);
		file = new File(dir, fileName);
		if (file.exists()) {
			cacheBmp = BitmapTool.getFastBitmap(file.getAbsolutePath());
			imageCache.put(path, cacheBmp);
			imgbtn.setImageBitmap(cacheBmp);
			return;
		}
		imgbtn.setImageResource(R.drawable.ic_stub);
		new Thread() {
			public void run() {
				Bitmap bmp = null;
				if (HttpUtils.download(MyApplication.ServerUrl + path,
						fileName, dir)) {
					bmp = BitmapTool.getFastBitmap(new File(dir, fileName).getAbsolutePath());
					imageCache.put(path, bmp);
				} else {
					bmp = bmp_error;
				}
				final Bitmap img = bmp;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						if (imgbtn.getTag().equals(path)) {
							imgbtn.setImageBitmap(img);
						}
					}
				});
			}
		}.start();
	}

	public void viewImage(String path) {
		String uri ="file://";
		final File dir = mContext.getExternalCacheDir();
		uri += new File(dir,SimpleUtils.getFileNameFromPath(path)).getAbsolutePath();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(uri), "image/*");
		mContext.startActivity(i);
	}
}
