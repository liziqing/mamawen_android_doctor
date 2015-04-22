/**  
 * @Title: MarketFragment.java 
 * @Package cn.net_show.doctor.fragment 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author 王帅
 * @date 2015年2月18日 下午3:19:31  
 */ 
package cn.net_show.doctor.fragment;

import cn.net_show.doctor.R;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/** 
 * @ClassName: MarketFragment 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author 王帅
 * @date 2015年2月18日 下午3:19:31 
 *  
 */
public class MarketFragment extends Fragment {
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_market, container, false);
		findViews();
		return view;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void findViews(){
		WebView wv = (WebView) view.findViewById(R.id.webView);
		WebSettings setting = wv.getSettings();
		setting.setJavaScriptEnabled(true);
		setting.setSupportZoom(true);
		setting.setUseWideViewPort(true);			//与下面的设置项一起设置使网页支持自适应屏幕
		setting.setLoadWithOverviewMode(true);
		
		//设置加载网页过程中显示进度条  加载失败显示失败提示
		wv.setWebViewClient(new WebViewClient(){
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				if(getActivity()!=null){
					Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();
				}
				super.onReceivedError(view, errorCode, description, failingUrl);
			}	
		});
		wv.loadUrl("http://ai.m.taobao.com/?pid=mm_108200526_8796020_29644823");
		
	}
}
