package com.honestwalker.android.commons.jscallback.actions;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.views.loading.Loading;

/**
 * Created by honestwalker on 15-7-8.
 */
public class CloseLoadingAction extends JSCallbackAction<JSActionParamBean> {

    @Override
    protected String doAction(Activity context, JSActionParamBean paramBean, HtmlWebView webView) {
//        KancartReceiverManager.sendBroadcast(context,KancartReceiverManager.Action.ACTION_CLOSE_LOADING);   //发送广播关闭loading
        Loading.dismiss(context);
        return "";
    }

}
