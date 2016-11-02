package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.OpenUrlParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;

/**
 * OpenUrl类型的 js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class OpenUrlAction extends JSCallbackAction<OpenUrlParamBean> {

    @Override
    protected String doAction(Activity context , OpenUrlParamBean paramBean , HtmlWebView webView) {
        LogCat.d("js_callback" , "OpenUrlAction doAction");
        return "";

    }

}
