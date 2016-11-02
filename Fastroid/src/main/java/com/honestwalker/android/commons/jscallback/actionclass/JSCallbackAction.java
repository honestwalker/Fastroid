package com.honestwalker.android.commons.jscallback.actionclass;

import android.app.Activity;

import com.google.gson.Gson;
import com.honestwalker.android.commons.jscallback.bean.JSActionConfigBean;
import com.honestwalker.android.commons.jscallback.bean.JSActionParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * JS回调抽象父类，负责参数实体对象封装，调用子类业务实现(doAction)。
 * Created by honestwalker on 15-6-2.
 */
public abstract class JSCallbackAction<T extends JSActionParamBean> {

    private String paramJson;

    private T paramBean;

    protected abstract String doAction(Activity context, T paramBean, HtmlWebView webView);

    public String execute(final Activity context, JSActionConfigBean jsCallbackAction, String paramJson, final HtmlWebView webView) {

        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType =  (ParameterizedType)type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<T> tClass = (Class<T>)actualTypeArguments[0];
        paramBean = (T) new Gson().fromJson(paramJson , tClass);
        /*UIHandler.post(new Runnable() {
            @Override
            public void run() {

            }
        });*/
        String result = doAction(context, paramBean, webView);

        return result;
    }

    public String getParamJson() {
        return paramJson;
    }

    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
    }
}
