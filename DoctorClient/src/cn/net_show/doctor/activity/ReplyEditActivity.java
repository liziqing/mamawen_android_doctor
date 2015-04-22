/**  
 * @Title: ReplyEditActivity.java 
 * @Package cn.net_show.doctor.activity 
 * @author 王帅
 * @date 2015年3月22日 上午12:33:23  
 */
package cn.net_show.doctor.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import mark.utils.SimpleUtils;

import org.litepal.crud.DataSupport;

import cn.net_show.doctor.MyApplication;
import cn.net_show.doctor.R;
import cn.net_show.doctor.adapter.FastReplyAdapter;
import cn.net_show.doctor.model.FastReplyItem;
import cn.net_show.doctor.service.PlayVoiceService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @ClassName: ReplyEditActivity
 * @author 王帅
 * @date 2015年3月22日 上午12:33:23
 */
public class ReplyEditActivity extends FragmentActivity {
	private View bottomView;
	private ListView listView;
	private MyApplication app;
	private List<FastReplyItem> replyList;
	private TranslateAnimation show, hide;
	private Button btn_record;
	private CheckBox cb_type;
	private File audio;
	private FastReplyItem replyItem;
	private long timeFlag;
	private EditText edt_subject, edt_content;
	private MediaRecorder recorder;
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_add:
			replyItem = new FastReplyItem();
			audio = null;
			
			if(cb_type.getVisibility()==View.GONE){
				cb_type.setVisibility(View.VISIBLE);
			}
			if(bottomView.getVisibility()==View.GONE){
				bottomView.setVisibility(View.VISIBLE);
				bottomView.startAnimation(show);
			}
			edt_content.setText("");
			edt_subject.setText("");
			cb_type.setChecked(false);
			break;
		case R.id.btn_save:
			save();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle bundle) {
		init();
		setContentView(R.layout.activity_fast_reply);
		findViews();
		super.onCreate(bundle);
		replyItem = new FastReplyItem();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	private void init() {
		app = (MyApplication) getApplication();
	}

	private void findViews() {
		hide = (TranslateAnimation) AnimationUtils.loadAnimation(this,
				R.anim.slide_top_bottom);
		show = (TranslateAnimation) AnimationUtils.loadAnimation(this,
				R.anim.slide_bottom_top);
		bottomView = findViewById(R.id.bottom);
		btn_record = (Button) findViewById(R.id.btn_record);
		btn_record.setOnTouchListener(new OnTouchListener() {			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						audio = new File(
								getExternalFilesDir(Environment.DIRECTORY_DCIM),
								app.Doctor.getJid() + "-"+ System.currentTimeMillis() + ".amr");
						audio.createNewFile();
						timeFlag = System.currentTimeMillis();
						recordVice(audio.getCanonicalPath());
						break;
					case MotionEvent.ACTION_UP:
						if (System.currentTimeMillis() - timeFlag < 1000) {
							Toast.makeText(ReplyEditActivity.this, "录音时间过短！",Toast.LENGTH_SHORT).show();
							audio.delete();
							return false;
						}
						recorder.stop();
						recorder.reset();
						recorder.release();
						recorder = null;
						replyItem.setType(FastReplyItem.TYPE_VOICE);
						replyItem.setContent(audio.getAbsolutePath());
						break;
					}
					return false;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return false;
			}
		});
		edt_content = (EditText) findViewById(R.id.edt_detail);
		edt_subject = (EditText) findViewById(R.id.edt_subject);
		cb_type = (CheckBox) findViewById(R.id.check_type);
		cb_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					replyItem.setType(FastReplyItem.TYPE_VOICE);
					edt_content.setVisibility(View.GONE);
					btn_record.setVisibility(View.VISIBLE);
				} else {
					replyItem.setType(FastReplyItem.TYPE_STRING);
					btn_record.setVisibility(View.GONE);
					edt_content.setVisibility(View.VISIBLE);
				}
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FastReplyItem item = replyList.get((int)id);
				//如果是声音则播放  如果bottomView在显示则隐藏
				if(item.getType() == FastReplyItem.TYPE_VOICE){
					playVoice(item.getContent());
					if (bottomView.getVisibility() == View.VISIBLE) {
						bottomView.startAnimation(hide);
						bottomView.setVisibility(View.GONE);
					}					
					return;
				}
				//如果不是录音 则显示编辑  如bottomView隐藏则显示出来
				if (bottomView.getVisibility() == View.VISIBLE) {
					//bottomView.startAnimation(hide);
					//bottomView.setVisibility(View.GONE);
					//return;
				} else {					
					bottomView.setVisibility(View.VISIBLE);
					bottomView.startAnimation(show);
				}
				//显示文字编辑项
				cb_type.setChecked(false);
				if(cb_type.getVisibility()==View.VISIBLE){
					cb_type.setVisibility(View.GONE);					
				}
				replyItem = item;
				//将选中的条目的信息显示出来
				edt_subject.setText(item.getSubject());
				edt_content.setText(item.getContent());
			}
		});
		getReplyItem();
		listView.setAdapter(new FastReplyAdapter(replyList, this));
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		if (bottomView.getVisibility() == View.VISIBLE) {
			bottomView.startAnimation(hide);
			bottomView.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
		}
	}

	private void playVoice(String path) {
		Intent i = new Intent(this, PlayVoiceService.class);
		i.putExtra("PATH", path);
		startService(i);
	}

	/**
	 * 获取用户的快捷回复数据
	 * 
	 * @Title: getReplyItem
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return void
	 */
	private void getReplyItem() {
		replyList = DataSupport.where("owner='" + app.Doctor.getJid() + "'")
				.order("id desc").find(FastReplyItem.class);
	}
	
	private void save(){
		String subject = edt_subject.getText().toString().trim();
		if(subject.length()<1){
			Toast.makeText(this, "请输入主题！", Toast.LENGTH_SHORT).show();
			return;
		}
		if(cb_type.isChecked()){
			if(replyItem==null||replyItem.getContent()==null){
				Toast.makeText(this, "没有要保存的内容！", Toast.LENGTH_SHORT).show();
				return;
			}
			replyItem.setType(FastReplyItem.TYPE_VOICE);
		}else{
			String content = edt_content.getText().toString().trim();
			if(content.length()<1){
				Toast.makeText(this, "没有要保存的内容！", Toast.LENGTH_SHORT).show();
				return;
			}
			replyItem.setContent(content);
			replyItem.setType(FastReplyItem.TYPE_STRING);
		}
		replyItem.setOwner(app.Doctor.getJid());
		replyItem.setSubject(subject);
		replyItem.save();
		bottomView.startAnimation(hide);
		bottomView.setVisibility(View.GONE);
		getReplyItem();
		listView.setAdapter(new FastReplyAdapter(replyList, this));
	}
	
	/**
	 * 录音
	 * @Title: recordVice
	 * @param path
	 * @return void
	 */
	@SuppressLint("InlinedApi")
	private void recordVice(String path) {
		if (SimpleUtils.isExtraStorageEnable()) {
			recorder = new MediaRecorder();
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(path);
			// recorder.setAudioSamplingRate();
			try {
				recorder.prepare();
			} catch (IllegalStateException e) {
				Toast.makeText(this, "录音失败！", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			} catch (IOException e) {
				Toast.makeText(this, "存储错误,录音失败！", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
				return;
			}
			recorder.start();
		} else {
			Toast.makeText(this, "SD卡不可用,无法录音！", Toast.LENGTH_SHORT).show();
		}
	}
}
