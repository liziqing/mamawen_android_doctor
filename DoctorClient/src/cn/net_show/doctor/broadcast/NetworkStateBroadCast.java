/**  
 * @Title: NetworkStateBroadCast.java 
 * @Package cn.net_show.doctor.broadcast 
 * @author 王帅
 * @date 2015年2月28日 下午1:05:24  
 */ 
package cn.net_show.doctor.broadcast;

import cn.net_show.doctor.service.IMservice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** 
 * @ClassName: NetworkStateBroadCast 
 * @author 王帅
 * @date 2015年2月28日 下午1:05:24  
 */
public class NetworkStateBroadCast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, IMservice.class);
		context.startService(service);
	}

}
