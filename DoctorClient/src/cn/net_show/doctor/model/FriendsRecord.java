package cn.net_show.doctor.model;

import java.io.Serializable;

public class FriendsRecord {
/*
 * [{"friend":
 * {"userID":2,
 * "name":"Jack",
 * "nickname":null,
 * "avatar":null,
 * "cellPhone":"134556123",
 * "phase":"备孕",
 * "lastMensDate":null,
 * "mensCycle":null,
 * "exptBirthday":null,
 * "height":null,
 * "weightBeforePreg":null},
 * "lastRecord":{
 * "id":12,
 * "category":"宝宝",
 * "subCategory":"身高",
 * "value":6.6,
 * "cycle":4,
 * "recordDate":"2015-04-18",
 * "createTime":null,
 * "updatedTime":"2015-04-18"}},{"friend":{"userID":1,"name":"Dave","nickname":null,"avatar":null,"cellPhone":"134567123","phase":"备孕","lastMensDate":null,"mensCycle":null,"exptBirthday":null,"height":null,"weightBeforePreg":null},"lastRecord":{"id":27,"category":"宝宝","subCategory":"头围","value":25.3,"cycle":3,"recordDate":"2015-04-23","createTime":null,"updatedTime":"2015-04-23"}}]}
 */
	
//	 * "phase":"备孕",
//	 * "lastMensDate":null,
//	 * "mensCycle":null,
//	 * "exptBirthday":null,
//	 * "height":null,
//	 * "weightBeforePreg":null},
	public static class Friend implements Serializable{
	private static final long serialVersionUID = 1L;
		public int userID;
		public String name;
		public String nickname;
		public String avatar;
		public String cellPhone;
		public String email;
//		public String phase;
//		public String lastMensDate;
//		public String exptBirthday;
	}
	
	public static class Record{
		public int id;
		public String category;
		public String subCategory;
		public float value;
		public int cycle;
		public String recordDate;
		public String createTime;
		public String updatedTime;
	}
	
	public Record lastRecord;
	public Friend friend;
	             
}
