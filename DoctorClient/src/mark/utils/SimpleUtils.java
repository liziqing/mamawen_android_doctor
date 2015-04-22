/**  
 * @Title: SimpleUtils.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年3月2日 上午11:59:56  
 */
package mark.utils;
import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

/**
 * @ClassName: SimpleUtils
 * @author 王帅
 * @date 2015年3月2日 上午11:59:56
 */
public class SimpleUtils {
	/**
	 * @Title: Uri2File
	 * @Description: 将系统传过来的Uri转化为File文件描述符
	 * @param context
	 *            上下文
	 * @param uri
	 * @return File
	 */
	public static File Uri2File(Context context, Uri uri) {

		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor actualimagecursor = context.getContentResolver().query(uri,
				proj, null, null, null);

		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		actualimagecursor.moveToFirst();

		String img_path = actualimagecursor
				.getString(actual_image_column_index);

		File file = new File(img_path);
		Logger.i("FILE", "file size = " + file.length());
		actualimagecursor.close();

		return file;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static long randomTime(String beginDate, String endDate) {

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
			Date start = format.parse(beginDate);// 构造开始日期
			Date end = format.parse(endDate);// 构造结束日期
			// getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
			if (start.getTime() >= end.getTime()) {
				return 0;
			}
			long date = random(start.getTime(), end.getTime());
			//return new Date(date);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}

	private static long random(long begin, long end) {
		long rtn = begin + (long) (Math.random() * (end - begin));
		// 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
		if (rtn == begin || rtn == end) {
			return random(begin, end);
		}
		return rtn;
	}
	/**
	 * 获取外置存储路径
	 * @Title: getExtraStoragePath 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return   
	 * @return File
	 */
	public static File getExtraStoragePath(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File path = Environment.getExternalStorageDirectory(); 
			return path;
		}else{
			return null;
		}		
	}
	/**
	 * 判断外置存储是否可用
	 * @Title: isExtraStorageEnable 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @return   
	 * @return boolean
	 */
	public static boolean isExtraStorageEnable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 验证手机号
	 * @Title: isMobile 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param str
	 * @return   
	 * @return boolean
	 */
	public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    } 
	
	public static String encodePassword(String password){
		
		try {
			//String pd = DigestUtils.sha256Hex(password);
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] bytes = password.getBytes();
			md.update(bytes);
			byte[] b = md.digest();
			String str = bytes2HexString(b);
			Log.e("sha256", str);
			return Base64.encodeToString(str.getBytes(),Base64.NO_WRAP);
		} catch (Exception e) {
			e.printStackTrace();
			return password;
		}
		
	}
	
	public static String getFileNameFromPath(String path){
		if(path==null||"".equals(path.trim())){
			return null;
		}
		
		String[] arr = path.trim().split("/");
		
		return arr[arr.length-1];
	}
	
	
	public static String  bytes2HexString(byte[] bs){
		StringBuilder sb = new StringBuilder("");
        int bit;  
        char[] chars = "0123456789ABCDEF".toCharArray();  
        for (int i = 0; i < bs.length; i++) {  
            bit = (bs[i] & 0x0f0) >> 4;  
            sb.append(chars[bit]);  
            bit = bs[i] & 0x0f;  
            sb.append(chars[bit]);  
        } 
        
        return sb.toString();
	}
}
