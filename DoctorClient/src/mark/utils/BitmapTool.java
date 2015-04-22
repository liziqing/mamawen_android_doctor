/**  
 * @Title: BitmapTool.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年3月7日 上午1:59:08  
 */
package mark.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * @ClassName: BitmapTool
 * @author 王帅
 * @date 2015年3月7日 上午1:59:08
 */
public class BitmapTool {

	/**
	 * 以缩略图的方式读取本地资源的图片
	 * @param context
	 * @param resId
	 * @return
	 */

	public static Bitmap getFastBitmap(String path) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// opts.inPreferredConfig = Bitmap.Config.RGB_565;
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		int h = opts.outHeight;
		int w = opts.outWidth;

		if (h > 150 || w > 150) {
			int hradio = Math.round((float) h / 120);
			int wradio = Math.round((float) w / 120);
			int sampleSize = hradio < wradio ? hradio : wradio;
			// opts = new BitmapFactory.Options();
			opts.inSampleSize = sampleSize;
			opts.inJustDecodeBounds = false;
		}
		Bitmap bmp = null;

		try {
			bmp = BitmapFactory.decodeFile(path, opts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
		// return weak.get();
	}

	/**
	 * 
	 * @Title: zoomImg
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 * @return Bitmap
	 */
	// 缩放/裁剪图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	// 通过文件路径获取到bitmap
	public static Bitmap getBitmapFromPath(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		// 设置为ture只获取图片大小
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

		BitmapFactory.decodeFile(path, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0.f, scaleHeight = 0.f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
				BitmapFactory.decodeFile(path, opts));
		return Bitmap.createScaledBitmap(weak.get(), w, h, true);
	}

	public static Bitmap getBitmapFromRes(Resources res, int id, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
		BitmapFactory.decodeResource(res, id, opts);
		int width = opts.outWidth;
		int height = opts.outHeight;
		float scaleWidth = 0f, scaleHeight = 0f;
		if (width > w || height > h) {
			// 缩放
			scaleWidth = ((float) width) / w;
			scaleHeight = ((float) height) / h;
		}
		opts.inJustDecodeBounds = false;
		float scale = Math.max(scaleWidth, scaleHeight);
		opts.inSampleSize = (int) scale;
		// WeakReference<Bitmap> weak = new WeakReference<Bitmap>(
		// BitmapFactory.decodeResource(res, id, opts));
		// return Bitmap.createScaledBitmap(weak.get(), w, h, true);

		return BitmapFactory.decodeResource(res, id, opts);
	}
	
	public static void  bitmap2File(Bitmap bmp,File fImage){
		if(bmp==null||fImage==null){
			Logger.e("photo == null");
			return;
		}
		try {
			fImage.createNewFile();
			FileOutputStream iStream = new FileOutputStream(fImage); 
			bmp.compress(CompressFormat.PNG, 100, iStream); 
			iStream.close();
//		fImage.close();
			iStream =null;
//			fImage =null;
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
