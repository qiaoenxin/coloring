package com.cphone.colorphone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneReceiver extends BroadcastReceiver {
	
	private PhoneStateListener listener;
	private Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("action" + intent.getAction());
		this.context = context;
		// 如果是去电
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			Log.d("onReceive", "call OUT:" + phoneNumber);
			if(phoneNumber != null){
				if(phoneNumber.equals(PreferenceUtil.getStringFromSharedPreferences(context, PreferenceUtil.PERFER_KEY_COLOR))){
					startPop(context, "file:///android_asset/music.html", false);
				}else if(phoneNumber.equals(PreferenceUtil.getStringFromSharedPreferences(context, PreferenceUtil.PERFER_KEY_BOX))){
					startPop(context, "file:///android_asset/musicbox.html", false);
				}else if(phoneNumber.equals(PreferenceUtil.getStringFromSharedPreferences(context, PreferenceUtil.PERFER_KEY_COLOR_ADV))){
					startPop(context, "file:///android_asset/musicAd.html", true);
				}
			}
		} else {
			// 查了下android文档，貌似没有专门用于接收来电的action,所以，非去电即来电.
			// 如果我们想要监听电话的拨打状况，需要这么几步 :
			/*
			 * 第一：获取电话服务管理器TelephonyManager manager =
			 * this.getSystemService(TELEPHONY_SERVICE);
			 * 第二：通过TelephonyManager注册我们要监听的电话状态改变事件。manager.listen(new
			 * MyPhoneStateListener(),
			 * PhoneStateListener.LISTEN_CALL_STATE);这里的PhoneStateListener
			 * .LISTEN_CALL_STATE就是我们想要 监听的状态改变事件，初次之外，还有很多其他事件哦。 第三步：通过extends
			 * PhoneStateListener来定制自己的规则。将其对象传递给第二步作为参数。
			 * 第四步：这一步很重要，那就是给应用添加权限。android.permission.READ_PHONE_STATE
			 */
			// 设置一个监听器
			if(listener == null){
				listener = createListener();
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Service.TELEPHONY_SERVICE);
				tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
			}
		}
	}

	private void startPop(Context context, String url, boolean advertise){
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, FxService.class);
		intent.putExtra("advertise", advertise);
		intent.putExtra("url", url);
		//启动FxService
		context.startService(intent);
	}
	
	
	private PhoneStateListener createListener(){
		PhoneStateListener listener = new PhoneStateListener() {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				// 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
				super.onCallStateChanged(state, incomingNumber);
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					System.out.println("挂断" + context);
					if(context != null){
						Intent intent = new Intent(context, FxService.class);
						context.stopService(intent);
					}
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					System.out.println("接听");
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					System.out.println("响铃:来电号码" + incomingNumber);
					// 输出来电号码
					break;
				}
			}
		};
		return listener;
	}
	
	
	
}
