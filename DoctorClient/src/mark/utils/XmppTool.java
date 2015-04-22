package mark.utils;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import cn.net_show.doctor.MyApplication;
import android.content.Context;
import android.util.Log;

public class XmppTool {
	private static final String TAG="IMconnection";
	private static XMPPConnection con = null;
	private static AccountManager accountManager;
	//private static boolean isRunning = false;
	private static void openConnection(final Context context) {
		try {
			//url、端口，也可以设置连接的服务器名字，地址，端口，用户。   
			//ConnectionConfiguration connConfig = new ConnectionConfiguration("192.168.253.1", 5222);
			ConnectionConfiguration connConfig = new ConnectionConfiguration("182.254.222.156", 5222);
			connConfig.setReconnectionAllowed(true);//允许重连
			connConfig.setSendPresence(true);		//允许更新状态
			connConfig.setSASLAuthenticationEnabled(true);
			connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);  
			con = new XMPPConnection(connConfig);
			con.connect();	
			//清空对话窗口
			IMChatTool.clear();
			//注册会话窗口管理的监听器
			con.getChatManager().addChatListener(new ChatManagerListener() {			
				@Override
				public void chatCreated(Chat chat, boolean able) {
					Log.i(TAG,"Participant:"+chat.getParticipant());					
					chat.addMessageListener(new ChatMessageListener(context));
					IMChatTool.addChat(chat.getParticipant().split("@")[0], chat);
				}
			});
			//注册文件接收的监听器
			//FileTransferManager ffm = new FileTransferManager(con);
			//ffm.addFileTransferListener(new ReceiverFileListener(context));
		}catch (Exception xe) {
			xe.printStackTrace();
		}
	}

	public synchronized static XMPPConnection getConnection(Context context) {		
		if (con == null) {
			openConnection(context);
		}	
//		if(!con.isConnected() && !isRunning){
//			isRunning = true;
//			new Thread(){
//				public void run(){				
//					try {
//						con.connect();
//					} catch (XMPPException e) {
//						Log.i(TAG,"连接服务器失败");
//						e.printStackTrace();
//					}
//					isRunning = false;
//				}			
//			}.start();	
//		}
		return con;
	}
	
	public static void closeConnection() {
		IMChatTool.clear();
		if(con!=null && con.isConnected()){
			con.disconnect();
			con = null;
		}
	}
	
	public static AccountManager getAccountManager(Context context){
		if (con == null) {
			openConnection(context);
		}
		if(con.isConnected()){
			accountManager = con.getAccountManager();
		}
		return accountManager;
	}
	/**
	 * @Title: login 
	 * @Description: 登陆方法
	 * @param name
	 * @param passwd
	 * @param context 
	 * @throws XMPPException   
	 * @return void
	 */
	public static void login(String name,String passwd,Context context) throws XMPPException{
//		XMPPConnection conn= getConnection(context);
//		if(conn.isAuthenticated()){
//			closeConnection();
//			conn= getConnection(context);
//		}
		closeConnection();
		XMPPConnection conn= getConnection(context);
		if(!conn.isAuthenticated()){
			IMChatTool.clear();
			conn.login(name, passwd,MyApplication.MARK);			
		}
	}
	
	
	
}
