/**  
 * @Title: IMChatUtil.java 
 * @Package cn.net_show.doctor.utils 
 * @author 王帅
 * @date 2015年3月1日 下午1:20:39  
 */ 
package mark.utils;

import java.util.HashMap;

import org.jivesoftware.smack.Chat;

/** 
 * @ClassName: IMChatUtil 
 * @author 王帅
 * @date 2015年3月1日 下午1:20:39  
 */
public class IMChatTool {
	private static HashMap<String,Chat> set;
	
	public static Chat getChatByUser(String user){
		if(set==null){
			return null;
		}
		return set.get(user);
	}
	
	public static void addChat(String user,Chat chat){
		if(set==null){
			set=new HashMap<String, Chat>();
		}
		if(null==user || null==chat){
			return;
		}
		set.put(user, chat);
	}
	
	public static void clear(){
		if(set!=null){
			set.clear();
		}
	}
	
}
