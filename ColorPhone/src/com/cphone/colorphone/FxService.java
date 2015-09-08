package com.cphone.colorphone;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FxService extends Service 
{

	//定义浮动窗口布局
	RelativeLayout mFloatLayout;
    WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	
	Button closeBtn;
	
	WebView webView;
	
	private static final String TAG = "FxService";
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "oncreat");
		createFloatView();		
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null){
			return super.onStartCommand(intent, flags, startId);
		}
		String url = intent.getStringExtra("url");
		boolean hasAdv = intent.getBooleanExtra("advertise", false);
		webView.loadUrl(url);//"file:///android_asset/musicbox.html"
		if(hasAdv){
			android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams)webView.getLayoutParams();
//			params.height = 800;//500
//			webView.setLayoutParams(params);
//			wmParams.height = 400;
			//mWindowManager.updateViewLayout(mFloatLayout, wmParams);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	private void createFloatView()
	{
		wmParams = new WindowManager.LayoutParams();
		//获取的是WindowManagerImpl.CompatModeWrapper
		mWindowManager = (WindowManager)getApplication().getSystemService(WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager--->" + mWindowManager);
		//设置window type
		wmParams.type = LayoutParams.TYPE_PHONE; 
		//设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888; 
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;      
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.CENTER;  //Gravity.LEFT | Gravity.TOP;       
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        String point = PreferenceUtil.getStringFromSharedPreferences(this, PreferenceUtil.PERFER_KEY_TOP_X_Y);
        if(point != null && point.length() > 0){
        	wmParams.gravity = Gravity.LEFT | Gravity.TOP;     
        	String[] splits = point.split(",");
        	wmParams.x = Integer.valueOf(splits[0]);
        	wmParams.y = Integer.valueOf(splits[1]);
        }
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;   
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据  
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		 /*// 设置悬浮窗口长宽数据
        wmParams.width = 200;
        wmParams.height = 80;*/
   
        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_window, null);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);
        //浮动窗口按钮
        closeBtn = (Button)mFloatLayout.findViewById(R.id.fload_id);
        
        webView = (WebView) mFloatLayout.findViewById(R.id.webView);
//         webView.getMeasuredWidth()/2；
        setupWebView(webView);
        
        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Log.i(TAG, "Width/2--->" + closeBtn.getMeasuredWidth()/2);
        Log.i(TAG, "Height/2--->" + closeBtn.getMeasuredHeight()/2);
        Button winBar = (Button)mFloatLayout.findViewById(R.id.win_bar);
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
			}
		}, 1000);
        
        winBar.setOnTouchListener(new OnTouchListener() {
			private boolean start;
			int startX;
			int startY;
			int x;
			int y;
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					start = true;
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					x = wmParams.x;
					y = wmParams.y;
					return false;
				}
				if(event.getAction() == MotionEvent.ACTION_MOVE && start){
					wmParams.x = x + ((int)event.getRawX() - startX);
					wmParams.y = y + ((int)event.getRawY() - startY);
					mWindowManager.updateViewLayout(mFloatLayout, wmParams);
					return true;
				}
				if(event.getAction() == MotionEvent.ACTION_CANCEL){
					start = false;
					saveLocation();
					return false;
				}
				if(event.getAction() == MotionEvent.ACTION_UP){
					start = false;
					saveLocation();
					return false;
				}
				return false;
			}
		});
        
        closeBtn.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v) 
			{
				//移除悬浮窗口
				stopSelf();
			}
		});
	}
	
	private void saveLocation(){
		
		String value = wmParams.x + "," + wmParams.y;
		System.out.println("location:" + value);
		PreferenceUtil.putStringFromSharedPreferences(this, PreferenceUtil.PERFER_KEY_TOP_X_Y, value);;
	}
	
	private void setupWebView(WebView m_webView){
		m_webView.setHorizontalScrollBarEnabled(false);
		m_webView.setHorizontalScrollbarOverlay(false);
		m_webView.setVerticalScrollBarEnabled(false);
		m_webView.setVerticalScrollbarOverlay(false);
		m_webView.setScrollbarFadingEnabled(false);
		m_webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = m_webView.getSettings();
		settings.setBuiltInZoomControls(false);
		settings.setJavaScriptEnabled(true);
	}
	
	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mFloatLayout != null)
		{
			//移除悬浮窗口
			mWindowManager.removeView(mFloatLayout);
		}
	}
}