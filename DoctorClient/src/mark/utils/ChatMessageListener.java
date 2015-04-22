/**  
 * @Title: ChartMessageListener.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年3月1日 下午1:23:14  
 */ 
package mark.utils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import com.google.gson.Gson;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.model.InquiryItem;
import cn.net_show.doctor.model.MsgItem;
import cn.net_show.doctor.model.ServerMsgItem;
import cn.net_show.doctor.model.database.DbInquiry;
import cn.net_show.doctor.model.database.DbUser;
import cn.net_show.doctor.service.NotifyService;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.DialerFilter;

/** 
 * @ClassName: ChartMessageListener 
 * @author 王帅
 * @date 2015年3月1日 下午1:23:14  
 */
public class ChatMessageListener implements MessageListener{
	public static final String MESSAGE_ACTION="cn.net_show.fileReceiver";
	private static final String TAG="IMconnection";
	protected Context mContext;
	
	/**
	 * @param mContext
	 */
	public ChatMessageListener(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public void processMessage(Chat chat, Message msg) {
		Log.i(TAG, "from:"+msg.getFrom());
		Log.i(TAG, "body:"+msg.getBody());	
		Log.i(TAG, "part:"+chat.getParticipant());
		Gson gson = new Gson();
		ServerMsgItem message = gson.fromJson(msg.getBody(),ServerMsgItem.class);
		if(message==null){
			return;
		}
		MyApplication app = (MyApplication) mContext.getApplicationContext();
		MsgItem item = new MsgItem();
		item.setUser2(message.sender.getId());
		item.setUser1(app.Doctor.getDoctorID());
		item.setTime(System.currentTimeMillis());
		item.setIsSend(MsgItem.RECEIVE);
		item.setStatus(MsgItem.STATUS_UNREAD);
		if(message.category==ServerMsgItem.TYPE_CHAT){
			item.setType(message.subCategory);
			item.setContent(message.content);
		}else if(message.category==ServerMsgItem.TYPE_INQUIRY){
			item.setType(MsgItem.TYPE_INQUIRY);
			item.setContent(message.sender.getName()+"为您接诊！");
		}else if(message.category==ServerMsgItem.TYPE_REPORT){
			item.setType(MsgItem.TYPE_REPORT);
			item.setContent(message.description+"&**&"+message.suggestion);
		}else if(message.category==ServerMsgItem.TYPE_NEW){
			item.setInquiryId(message.inquiryID);
			item.setContent("您有新的问诊！");
			item.save();
			DbUser.saveIfNotExist(message.sender);
			InquiryItem inquiryItem = gson.fromJson(msg.getBody(), InquiryItem.class);
			inquiryItem.id = message.inquiryID;
			DbInquiry dbInquiry = DbInquiry.getDbModel(inquiryItem);
			dbInquiry.save();
			Intent i = new Intent(mContext,NotifyService.class);
			i.putExtra("message", item);
			i.putExtra("flag", 1);
			mContext.startService(i);
			return;
		}
		item.setInquiryId(message.inquiryID);
		item.save();
		Intent i = new Intent(MESSAGE_ACTION);
		i.putExtra("message", item);
		mContext.sendBroadcast(i);
		
	}

	
	
}
