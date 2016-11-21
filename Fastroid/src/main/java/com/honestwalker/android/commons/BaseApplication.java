package com.honestwalker.android.commons;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.otto.BusManager;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.system.ProcessUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Application父类，包涵了一些核心功能的加载
 * Created by honestwalker on 15-10-9.
 */
public class BaseApplication extends android.app.Application  {

    public static Context context = null;

    public static String appVersion = "";
    public static String appName = "";

    /** 记录程序上一次所在的页面名称 */
    public static String lastPage = "";

    /** 当前数据库版本 */
    private final int DATABASE_VERSION = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initApp();

        LogCat.d("PROC", "启动进程：" + ProcessUtil.getMyPid() +
                " 进程名：" + ProcessUtil.getCurProcessName(getApplicationContext()));

    }

    /** 做一些app初始化工作 */
    private void initApp() {

        appName = getResources().getString(R.string.app_name);
        appVersion = Application.getAppVersion(context, R.class);

        BusManager.init();

    }

    public static void exit() {
        Application.exit(context);
        System.exit(0);
    }

}
