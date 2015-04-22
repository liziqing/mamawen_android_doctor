/**  
 * @Title: HttpUtil.java 
 * @Package mark.utils.http 
 * @author 王帅
 * @date 2015年3月13日 上午10:35:23  
 */ 
package mark.utils.http;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import android.util.Log;
import com.google.gson.Gson;
import cn.net_show.doctor.model.ServerMsgItem;

/** 
 * @ClassName: HttpUtil 发送多文件请求
 * @author 王帅
 * @date 2015年3月13日 上午10:35:23  
 */
public class HttpUtil {
	private static HttpClient client;
	private static HttpUtil util;
	
	private HttpUtil(){
		
	}

	public static HttpUtil getInstance(){
		if(util==null){
			util = new HttpUtil();
			util.getHttpClient();
		}
		return util;
	}
	private HttpClient getHttpClient(){
		if(client==null){
			client= new DefaultHttpClient();
			HttpParams params = client.getParams();
			params.setIntParameter(HttpConnectionParams.SO_TIMEOUT, 15000); // 超时设置
	        params.setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 10000);// 连接超时
		}		
		return client;
	}

	public String mulitiPost(String url,List<ContentPart> list){
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setCharset(Charset.forName("utf-8"));
		try {
			for(ContentPart part : list){
				builder.addPart(part.key, part.body);
			}
			HttpEntity entity = builder.build();
			HttpPost post = new HttpPost(url);
			post.addHeader("charset", "UTF-8");
			post.setEntity(entity);
		
			HttpResponse response = client.execute(post);
			String result = EntityUtils.toString(response.getEntity());
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class ContentPart{
		public String key;
		public ContentBody body;
		
		/**
		 * @param key
		 * @param body
		 */
		public ContentPart(String key, ContentBody body) {
			this.key = key;
			this.body = body;
		}		
	}
	/**
	 * 发送聊天到服务器
	 * @Title: sendIMMessage 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param url
	 * @param item   
	 * @return String
	 */
	public String sendIMMessage(String url,ServerMsgItem item){ 	
		return sendIMMessage(url, item, null);
	}
	/**
	 * 发送聊天到服务器
	 * @Title: sendIMMessage  
	 * @param url
	 * @param item
	 * @param file 
	 * @return String
	 */
	public String sendIMMessage(String url,ServerMsgItem item,File file){
		if(url==null){
			return "url不能为空";
		}
		if(item==null){
			return "ServerMsgItem不能为空";
		}
		Gson gson = new Gson();
		ArrayList<ContentPart> list = new ArrayList<>();
		Log.e("message", gson.toJson(item));
		ContentPart part = new ContentPart("message",new StringBody(gson.toJson(item), ContentType.create("text/plain", Consts.UTF_8)));
		list.add(part);
		if(file!=null){
			part = new ContentPart("file", new FileBody(file));
			list.add(part);
		}	
		return mulitiPost(url, list);
	}
	
	
	
}
