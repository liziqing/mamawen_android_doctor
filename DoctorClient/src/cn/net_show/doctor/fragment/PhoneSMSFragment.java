/**  
 * @Title: QRCodeFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月20日 下午6:03:17  
 */
package cn.net_show.doctor.fragment;

import cn.net_show.doctor.R;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @ClassName: QRCodeFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 王帅
 * @date 2015年2月20日 下午6:03:17
 * 
 */
public class PhoneSMSFragment extends Fragment implements OnClickListener {
	private View view;
	private ImageButton btn;
	private TextView textNumber;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.phone_sms, container, false);
		initView();
		return view;
		// return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void initView() {
		textNumber = (TextView) view.findViewById(R.id.phone_number);
		int[] arr = { R.id.num0, R.id.num1, R.id.num2, R.id.num3, R.id.num4,
				R.id.num5, R.id.num6, R.id.num7, R.id.num8, R.id.num9,
				R.id.btn_contract, R.id.btn_submit,R.id.btn_delete };
		for (int i : arr) {
			btn = (ImageButton) view.findViewById(i);
			btn.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.num0:
			setPhoneNumber(0);
			break;
		case R.id.num1:
			setPhoneNumber(1);
			break;
		case R.id.num2:
			setPhoneNumber(2);
			break;
		case R.id.num3:
			setPhoneNumber(3);
			break;
		case R.id.num4:
			setPhoneNumber(4);
			break;
		case R.id.num5:
			setPhoneNumber(5);
			break;
		case R.id.num6:
			setPhoneNumber(6);
			break;
		case R.id.num7:
			setPhoneNumber(7);
			break;
		case R.id.num8:
			setPhoneNumber(8);
			break;
		case R.id.num9:
			setPhoneNumber(9);
			break;
		case R.id.btn_contract:
			pickNumber();
			break;
		case R.id.btn_submit:
			submit();
			break;
		case R.id.btn_delete:
			deleteNumber();
			break;
		default:
			break;
		}
	}
	
	private void setPhoneNumber(int number){
		String str = textNumber.getText().toString();
		if(str.length()>=13){
			return;
		}
		str = str.replace("-", "");
		str = str + number;
		textNumber.setText(spliteNumber(str));
	}
	
	private String spliteNumber(String str){
		int length = str.length();
		if(length<=3){
			return str;
		}else if(length<=7){
			String sub = str.substring(0, 3);
			sub += "-";
			sub += str.substring(3);
			return sub;
		}else{
			String sub = str.substring(0, 3);
			sub += "-";
			sub += str.substring(3,7);
			sub += "-";
			sub += str.substring(7);
			return sub;
		}
	}
	
	
	private void deleteNumber(){
		String str = textNumber.getText().toString();
		if(str.length()<1){
			return;
		}
		str = str.replace("-", "");
		str = str.substring(0, str.length()-1);

		textNumber.setText(spliteNumber(str));
	}
	
	private void submit(){
		//TODO 待完善
		
	}
	
	private void pickNumber(){
		Intent intent = new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);
		startActivityForResult(intent, 1);  
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(resultCode==Activity.RESULT_OK){
			Uri contactData = data.getData();  
          //  @SuppressWarnings("deprecation")
			//Cursor cursor = getActivity().managedQuery(contactData, null, null, null,  null);
            Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null,  null);
            cursor.moveToFirst();  
            String num = getContactPhone(cursor);  
            textNumber.setText(spliteNumber(num));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getContactPhone(Cursor cursor) {  
        // TODO Auto-generated method stub   
        int phoneColumn = cursor  
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);  
        int phoneNum = cursor.getInt(phoneColumn);  
        String result = "";  
        if (phoneNum > 0) {  
            // 获得联系人的ID号   
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);  
            String contactId = cursor.getString(idColumn);  
            // 获得联系人电话的cursor   
            Cursor phone = getActivity().getContentResolver().query(  
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                    null,  
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="  
                            + contactId, null, null);  
            if (phone.moveToFirst()) {  
                for (; !phone.isAfterLast(); phone.moveToNext()) {  
                    int index = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);  
                    int typeindex = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);  
                    int phone_type = phone.getInt(typeindex);  
                    String phoneNumber = phone.getString(index);  
                    result = phoneNumber;  
//                  switch (phone_type) {//此处请看下方注释   
//                  case 2:   
//                      result = phoneNumber;   
//                      break;   
//   
//                  default:   
//                      break;   
//                  }   
                }  
                if (!phone.isClosed()) {  
                    phone.close();  
                }  
                if(!cursor.isClosed()){
                	cursor.close();
                }
            }  
        }  
        return result;  
    }  

}
