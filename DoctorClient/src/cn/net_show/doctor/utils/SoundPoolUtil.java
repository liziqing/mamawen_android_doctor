package cn.net_show.doctor.utils;

import java.util.HashMap;
import cn.net_show.doctor.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPoolUtil {
	private static SoundPool soundPool;
	private static Context mContext;
	private static HashMap<Integer, Integer> soundPoolMap;
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("deprecation")
	public static void init(Context context){
		mContext = context;
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		
		soundPoolMap = new HashMap<Integer, Integer>();   
		soundPoolMap.put(1, soundPool.load(mContext, R.raw.notify, 1)); 
	}
	
	
	public static void play(){
		soundPool.play(soundPoolMap.get(1), 1, 1, 0, 0, 1); 
	}
}
