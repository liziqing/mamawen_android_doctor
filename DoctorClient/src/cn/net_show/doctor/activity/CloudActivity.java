/**  
 * @Title: CloudActivity.java 
 * @Package cn.net_show.doctor.activity  
 * @author 王帅
 * @date 2015年2月20日 下午7:11:16  
 */ 
package cn.net_show.doctor.activity;

import cn.net_show.doctor.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;


/** 
 * @ClassName: CloudActivity 
 * @Description: 健康云 
 * @author 王帅
 * @date 2015年2月20日 下午7:11:16 
 *  
 */
public class CloudActivity extends FragmentActivity implements View.OnClickListener{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_cloud);
		findViews();
		init();
	}
	
	private void findViews(){
	
	}
	
	private void init(){
		
	}

	
	public void onClick(View v){
		switch(v.getId()){
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_contact_patient:
			startActivity(new Intent(this, IMActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		case R.id.btn_bingli:
			startActivity(new Intent(this, ReportActivity.class));
			overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			break;
		default:
			break;
		}
	}
	@Override
	public void finish() {		
		super.finish();
		overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
	}
}
