/**  
 * @Title: ReportItem.java 
 * @Package cn.net_show.doctor.model 
 * @author 王帅
 * @date 2015年3月23日 上午12:18:13  
 */ 
package cn.net_show.doctor.model;

import java.util.ArrayList;

/** 
 * @ClassName: ReportItem 
 * @author 王帅
 * @date 2015年3月23日 上午12:18:13  
 */
public class ReportItem {
//	 "gender": "M",
//    "inquiries": [{
//             "photos": [""],
//             "content": "测试问题1 妇科   Mark",
//             "createTime": 1427034865511,
//             "repportSuggestion": "哈哈哈",
//             "inquiryID": 1,
//             "description": "赵敏 8个月 女孩",
//             "reportDesc": "哈哈哈",
//             "mark": null,
//             "comment": null,
//             "drug": "该吃药了"
//     }],
//     "patientID": 2,
//     "age": 4,
//     "name": "Mike"
//}
	public String gender;
	public int patientID;
	public int age;
	public String name;
	public ArrayList<InquiryReportItem>  inquiries;
}
