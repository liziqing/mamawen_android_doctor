/**  
 * @Title: IMservice.java 
 * @Package cn.net_show.doctor.service 
 * @author 王帅
 * @date 2015年2月28日 下午1:03:32  
 */ 
package cn.net_show.doctor.service;

import mark.utils.NetworkStateTool;
import mark.utils.XmppTool;

import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import cn.net_show.doctor.MyApplication;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/** 
 * @ClassName: IMservice 
 * @author 王帅
 * @date 2015年2月28日 下午1:03:32  
 */
public class IMservice extends Service {
	private MyApplication app;
	
	private static final String TAG="IMconnection";
		
	@Override
	public void onCreate() {
		app = (MyApplication) getApplication();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
				
		if(NetworkStateTool.isNetworkAvailable(this)){
			new Thread(){
				@Override
				public void run(){
					bindIMConnection();
				}
			}.start();
			
		}else{
			XmppTool.closeConnection();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	private synchronized void bindIMConnection(){
		final XMPPConnection conn=XmppTool.getConnection(getApplication());
		if(app.Doctor==null){
			return;
		}
		if(!conn.isConnected()){
			try {
				conn.connect();
			} catch (XMPPException e1) {
				e1.printStackTrace();
				return;
			}
		}
		//判断IM是否已登陆
		if(conn.isAuthenticated()){
			return;
		}
		//判断应用是否已登陆
		/*if(!MyApplication.isLogin){
			return; 
		}*/
		try {
			//登陆添加监听
			app=(MyApplication) getApplication();
			XmppTool.login(app.Doctor.getJid(), app.Doctor.getImToken(),this);		
		} catch (XMPPException e) {
			Log.e(MyApplication.TAG,"IM Login failed");
			e.printStackTrace();
			return;
		}
		ConnectionListener conListener =  new ConnectionListener(){

			@Override
			public void reconnectionSuccessful() {
				Log.i(TAG, "重新连接成功！");
				if(MyApplication.Doctor!=null){
					if(!conn.isAuthenticated()){
						try {
							conn.login(MyApplication.Doctor.getJid(), MyApplication.Doctor.getImToken());
						} catch (XMPPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					};
				}
			}
			
			@Override
			public void reconnectionFailed(Exception e) {
				Log.e(TAG, "重连失败："+e.getMessage());
			}
			
			@Override
			public void reconnectingIn(int s) {
				Log.i(TAG, "正在重新连接:"+s);
			}
			
			@Override
			public void connectionClosedOnError(Exception e) {
				Log.e(TAG, "关闭连接异常："+e.getMessage());
				//conn.connect();
			}
			
			@Override
			public void connectionClosed() {
				Log.i(TAG, "关闭连接成功!");
			}
		};
		conn.addConnectionListener(conListener);
	}
	
	
}
