package cn.net_show.doctor.broadcast;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.service.NotifyService;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PushReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {

		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");
			
			String taskid1 = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean results = PushManager.getInstance().sendFeedbackMessage(context, taskid1, messageid, 90001);
			System.out.println("第三方回执接口调用" + (results ? "成功" : "失败"));
			
			if (payload != null) {
				String data = new String(payload);
				Log.d("GetuiSdkDemo", "Got Payload:" + data);
				Intent i = new Intent(context, NotifyService.class);
				i.putExtra("json", data);
				context.startService(i);
			}
			break;
		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");
			
			if(!MyApplication.clientID.equals(cid)){
				MyApplication.clientID = cid;
				Intent i = new Intent(context, NotifyService.class);
				i.putExtra("cid", cid);
				context.startService(i);
			}
			Log.d("GetuiSdkDemo", "Got ClientId:" + cid);
			break;
		case PushConsts.THIRDPART_FEEDBACK:
			String appid = bundle.getString("appid");
			String taskid = bundle.getString("taskid");
			String actionid = bundle.getString("actionid");
			String result = bundle.getString("result");
			long timestamp = bundle.getLong("timestamp");

			Log.d("GetuiSdkDemo", "appid = " + appid);
			Log.d("GetuiSdkDemo", "taskid = " + taskid);
			Log.d("GetuiSdkDemo", "actionid = " + actionid);
			Log.d("GetuiSdkDemo", "result = " + result);
			Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			break;
		default:
			break;
		}
	}
}
