package com.honestwalker.android.commons.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.honestwalker.android.commons.BaseApplication;
import com.honestwalker.android.commons.Constants.RequestCode;
import com.honestwalker.android.commons.Constants.ResultCode;
import com.honestwalker.android.commons.config.ContextProperties;
import com.honestwalker.android.commons.utils.IntentBind.IntentBinder;
import com.honestwalker.android.commons.utils.StartActivityHelper;
import com.honestwalker.android.kc_commons.ui.utils.TranslucentStatus;
import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.ViewUtils.ViewSizeHelper;
import com.honestwalker.androidutils.equipment.DisplayUtil;
import com.honestwalker.androidutils.equipment.SDCardUtil;
import com.honestwalker.androidutils.pool.ThreadPool;
import com.honestwalker.androidutils.views.AlertDialogPage;
import com.honestwalker.androidutils.views.loading.Loading;
import com.honestwalker.androidutils.window.DialogHelper;
import com.honestwalker.androidutils.window.ToastHelper;
import com.lidroid.xutils.ViewUtils;

/**
 * Created by honestwalker on 13-8-8.
 */
public abstract class BaseActivity extends FragmentActivity {
    
	//================================
	//
	//            公共控件
	//
	//================================
	
	//================================
	//
	//            公共参数
	//
	//================================

	protected LayoutInflater inflater;
	protected BaseActivity   context;
	
	public static ViewSizeHelper viewSizeHelper;

	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static int statusBarHeight = 0;

	protected int backAnimCode = 0;
	
	public View contentView;
	
	private long onResumeTime = 0;

	private KCTestLauncher kcTestLauncher;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;

		// 侵入时标题支持
		TranslucentStatus.setEnable(this);

		ViewUtils.inject(this);

		init();

		// 执行字段绑定器
		new IntentBinder().doIntentBind(this);

		onMeasure();

		loadData();

		kcTestLauncher = new KCTestLauncher();
		kcTestLauncher.start(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		
		BaseApplication.lastPage = this.getClass().getSimpleName();
		
		if(getIntent() != null) {
			backAnimCode = getIntent().getIntExtra("backAnimCode", 0);
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		onResumeTime = System.currentTimeMillis();
		kcTestLauncher.next(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		
		if(System.currentTimeMillis() - onResumeTime < 400) { // 避免连续按后退动画瑕疵
			return;
		}
		
		super.onBackPressed();
		finish();

		if(backAnimCode != 0) {
			StartActivityHelper.activityAnim(context, getIntent(), backAnimCode);
			backAnimCode = 0;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	/**
	 * 初始化公共参数
	 */
	public void init() {
		if (viewSizeHelper == null) {
			viewSizeHelper = ViewSizeHelper.getInstance(this);
		}
		if (screenWidth == 0) {
			screenWidth = DisplayUtil.getWidth(context);
			screenHeight = DisplayUtil.getHeight(context);
			statusBarHeight = DisplayUtil.getStatusBarHeight(context);
//			titleHeight = (int) (screenHeight * titleHeightScale);
		}
		if (inflater == null) {
			inflater = getLayoutInflater();
		}
	}

	/**
	 * 待重写方法，用于对控件进行显示设置，如：距离，位置，大小等等
	 */
	protected void onMeasure() {}

	protected abstract void loadData();

	private View layoutResView;
	public View getLayoutResView(){return layoutResView;}
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		this.layoutResView = view;
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		this.layoutResView = view;
	}
	
	protected Activity getContext() {
		context = this;
		return context;
	}

	/**
	 * 获取缓存路径 ， 末尾已经包含 /
	 */
	public String getCachePath() {
		return ContextProperties.getConfig().cachePath;
	}

	public String getImageCachePath() {
		return ContextProperties.getConfig().cachePath + "image/";
	}

	public String getSDCachePath() {
		return SDCardUtil.getSDRootPath() + ContextProperties.getConfig().sdcartCacheName + "/";
	}

	public void recyleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == RequestCode.ACTION_LOGIN) {
			if(resultCode == ResultCode.RESULT_CANCELED) {
//				loginCancleCallback();
			} else if(resultCode == ResultCode.RESULT_OK) {
				// 登录成功回调
//				loginSuccessCallback(getLoginSuccessUser());
			}
		}
	}
	
	/** 启动线程 */
	public void threadPool(Runnable runnable) {
		ThreadPool.threadPool(runnable);
	}
	
	/*=================================
	 * 
	 *             Intent 相关操作开始
	 * 
	 *=================================*/
	
	public Intent getIntent() {
		return super.getIntent() == null ? new Intent():super.getIntent();
	}
	
	/** 从intent取得一个Serializable对象 */
	public Object getIntentSerializableExtra(String key){
		Intent intent = getIntent();
		Object value  = intent.getSerializableExtra(key);
		return value;
	}
	
	public Object getIntentSerializableExtra(Intent intent , String key){
		Object value = intent.getSerializableExtra(key);
		return value;
	}
	
	/*=================================
	 * 
	 *      Intent 相关操作结束
	 * 
	 *=================================*/
	
	/*=================================
	 * 
	 *             公共控件操作
	 * 
	 *=================================*/

	public void setSize(View view , float width , float height) {
		setSize(view , (int)width , (int)height);
	}

	public void setSize(View view , int width , int height) {
		viewSizeHelper.setSize(view, width, height);
	}

	public void setSize(int viewResId , float width , float height) {
		setSize(viewResId , (int)width , (int)height);
	}

	public void setSize(int viewResId , int width , int height) {
		View view = findViewById(viewResId);
		setSize(view, width, height);
	}

	public void setWidth(View view,int width) {
		viewSizeHelper.setWidth(view, width);
	}

	public void setWidth(int viewResId , int width) {
		View view = findViewById(viewResId);
		setWidth(view, width);
	}

	public void setWidth(int viewResId , int width , int scaleWidth, int scaleHeight) {
		View view = findViewById(viewResId);
		setWidth(view, width, scaleWidth, scaleHeight);
	}

	public void setWidth(View view , int width , int scaleWidth, int scaleHeight) {
		int height = width * scaleHeight / scaleWidth;
		setWidth(view, width);
		setHeight(view, height);
	}

	public void setHeight(View view,int height) {
		try{
			LayoutParams lp = view.getLayoutParams();
			lp.height = height;
		} catch (Exception e) {
		}
	}

	public void setHeight(int viewResId,int height) {
		try{
			LayoutParams lp = findViewById(viewResId).getLayoutParams();
			lp.height = height;
		} catch (Exception e) {
		}
	}

	public void loading(final boolean show) {
		if(show) {
			Loading.show(context , "loading_cancelunable");
		} else {
			Loading.dismiss(context);
		}
	}

	public void loadingCancelAble(final boolean show) {
		if(show) {
			Loading.show(context , "loading_cancelable");
		} else {
			Loading.dismiss(context);
		}
	}
	
	public OnClickListener backBtnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			onBackPressed();
		}
	};

	/*=================================
	 *
	 *         公共控件操作结束
	 *
	 *=================================*/

	/*================================
	 * 
	 *            页面跳转相关 开始
	 * 
	 *================================ */

	public void toActivity(Class<? extends Activity> descActivityClass) {
		StartActivityHelper.toActivity(this, descActivityClass);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Intent intent) {
		StartActivityHelper.toActivity(this, descActivityClass, intent);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, animCode);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Bundle data, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, data , animCode);
	}

	public void toActivity(Class<? extends Activity> descActivityClass, Intent intent, int animCode) {
		StartActivityHelper.toActivity(this, descActivityClass, intent , animCode);
	}
	
	public void toActivityForResult(Class<? extends Activity> descActivityClass,int requestCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, requestCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass, Intent intent ,  int requestCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, intent , requestCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass , int requestCode , int animCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, requestCode , animCode);
	}

	public void toActivityForResult(Class<? extends Activity> descActivityClass, Intent intent, int requestCode , int animCode) {
		StartActivityHelper.toActivityForResult(this, descActivityClass, intent , requestCode , animCode);
	}
	
	/*================================
	 * 
	 *            页面跳转相关 结束
	 * 
	 *===============================*/
	
	

	/*================================
	 * 
	 *             对话框相关
	 * 
	 *===============================*/
	public void alertDialog(final String title, final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				DialogHelper.alert(BaseActivity.this, title, msg);
				AlertDialogPage dialog = new AlertDialogPage(context , AlertDialogPage.AlertDialogStyle.SingleBTN);
				dialog.setContent(msg);
				dialog.setTitle(title);
				dialog.setTitleVisible(true);
				dialog.show();
			}
		});
	}

	public void alertDialog(final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
//				DialogHelper.alert(BaseActivity.this, msg);
				AlertDialogPage dialog = new AlertDialogPage(context , AlertDialogPage.AlertDialogStyle.SingleBTN);
				dialog.setContent(msg);
				dialog.setTitleVisible(false);
				dialog.show();
			}
		});
	}
	
	public void alertToast(final String msg) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				ToastHelper.alert(BaseActivity.this, msg);
			}
		});
	}

	public void alertToast(final String msg, final int time) {
		UIHandler.post(new Runnable() {
			@Override
			public void run() {
				ToastHelper.alert(BaseActivity.this, msg, time);
			}
		});
	}

	/*================================
	 * 
	 *          对话框相关结束
	 *
	 *===============================*/

}
