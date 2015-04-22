/**  
 * @Title: PlayVoiceServer.java 
 * @Package cn.net_show.doctor.service 
 * @author 王帅
 * @date 2015年3月9日 下午4:46:44  
 */ 
package cn.net_show.doctor.service;

import java.io.IOException;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

/** 
 * @ClassName: PlayVoiceServer 
 * @author 王帅
 * @date 2015年3月9日 下午4:46:44  
 */
public class PlayVoiceService extends Service {
	private MediaPlayer mPlayer;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer = new MediaPlayer();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mPlayer!=null && mPlayer.isPlaying()){
			mPlayer.stop();
			//mPlayer.release();
		}
		mPlayer.reset();
		/*mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
		        @Override
		        public void onCompletion(MediaPlayer mp) {
		            // TODO Auto-generated method stub
		            mp.stop();
		            mp.release();
		        }
		});*/
		final String path = intent.getStringExtra("PATH");
		Log.i("voice","path="+path);
		try {
			//File file = new File(path);
			//@SuppressWarnings("resource")
			//FileInputStream fis = new FileInputStream(file); 
			//mPlayer.setDataSource(fis.getFD());
			mPlayer.setDataSource(path);	
			mPlayer.prepareAsync();
			mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
				}
			});
			//mPlayer.prepare();
			//mPlayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		if(mPlayer!=null){
			if(mPlayer.isPlaying()){
				mPlayer.stop();
			}
			mPlayer.release();
		}
		super.onDestroy();
	}
	
	

}
