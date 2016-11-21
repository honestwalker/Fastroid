package com.honestwalker.android.APICore;

import android.app.Activity;

import com.honestwalker.android.APICore.API.APIListener;
import com.honestwalker.android.APICore.API.ApiException;
import com.honestwalker.android.APICore.API.resp.BaseResp;
import com.honestwalker.android.APICore.event.APIFailEvent;
import com.honestwalker.android.APICore.event.APIReqEvent;
import com.honestwalker.android.APICore.event.APIRespEvent;
import com.honestwalker.android.otto.BusManager;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.squareup.otto.Subscribe;

import java.lang.reflect.Method;

/**
 * Created by lanzhe on 16-11-14.
 */
public final class APIMessage {

    public static APIMessage instance;

    private APIMessage() {}

    /**
     * 获取APIMessage 单例对象
     * @return
     */
    public static APIMessage getInstance() {
        if(instance == null) {
            instance = new APIMessage();
        }
        return instance;
    }

    /**
     * 发送api消息
     * @param subscriber API订阅者对象
     * @param req        req  请求对象
     */
    public static void send(APISubscriber subscriber, Object req) {
        BusManager.post(new APIReqEvent(subscriber, req));
    }

    /**
     * 注册API事件监听     <br />
     * 监听  APIReqEvent APIRespEvent APIFailEvent 等事件
     */
    public void register() {
        BusManager.register(this);
    }

    /**
     * 接收 API 请求事件
     * @param apiReqEvent
     */
    @Subscribe
    public void onAPISend(final APIReqEvent apiReqEvent) {

        int port = apiReqEvent.getAPISubscriber().getSuccessPort();

        // 找到回调的方法
        Method subscribeMethod = findSubscribeMethod(port, apiReqEvent.getAPISubscriber().getSubscriber());
        if(subscribeMethod == null) return;

        // 获得回调方法的参数类型
        Class<?>[] types = subscribeMethod.getParameterTypes();
        Class respClass = null;
        if(types != null && types.length > 0) {
            respClass = types[0];
        }
        if(respClass == null) { return; }

        // 开始请求
        OttoAPI ottoAPI = new OttoAPI((Activity)apiReqEvent.getAPISubscriber().getSubscriber());
        ottoAPI.request(apiReqEvent.getData(), respClass, new APIListener() {
            @Override
            public void onStart() {}

            @Override
            public void onComplete(Object resp) {
                LogCat.d("api", "请求成功 发出请求结果消息");
                APIRespEvent apiRespEvent = new APIRespEvent(apiReqEvent.getAPISubscriber(), resp);
                BusManager.post(apiRespEvent);
            }

            @Override
            public void onFail(ApiException e) {
                LogCat.d("api", "请求失败 发出请求结果消息");
                APIFailEvent apiFailEvent = new APIFailEvent(apiReqEvent.getAPISubscriber(), e);
                BusManager.post(apiFailEvent);
            }
        });

    }


    /**
     * 接受API请求完毕，发出的消息
     * @param apiRespEvent   API响应 事件
     */
    @Subscribe
    public void onAPIResp(APIRespEvent apiRespEvent) {
        LogCat.d("api", "接收到请求结果消息");
        int    port = apiRespEvent.getAPISubscriber().getSuccessPort();
        Object subscriber = apiRespEvent.getAPISubscriber().getSubscriber();

        Method subscribeMethod = findSubscribeMethod(port, subscriber);
        try {
            BaseResp resp = (BaseResp) apiRespEvent.getData();
            LogCat.d("api", "调用方法 " + subscribeMethod.getName() + "  传入参数 " + resp);
            subscribeMethod.invoke(subscriber, resp);
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }

    }

    /**
     * 接受API请求完毕，发出的消息
     * @param apiFailEvent      API 请求失败事件
     */
    @Subscribe
    public void onAPIFail(APIFailEvent apiFailEvent) {
        LogCat.d("api", "接收到请求结果消息");
        int    port = apiFailEvent.getAPISubscriber().getFailPort();
        Object subscriber = apiFailEvent.getAPISubscriber().getSubscriber();

        Method subscribeMethod = findSubscribeMethod(port, subscriber);
        try {
            ApiException e = (ApiException) apiFailEvent.getData();
            LogCat.d("api", "调用方法 " + subscribeMethod.getName() + "  传入参数 " + e);
            subscribeMethod.invoke(subscriber, e);
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }

    }

    private Method findSubscribeMethod(int port, Object subscriber) {

        Method[] subscribeMethods = subscriber.getClass().getDeclaredMethods();
        for (Method subscribeMethod : subscribeMethods) {
            subscribeMethod.setAccessible(true);
            APISubscribe subscribeanno = subscribeMethod.getAnnotation(APISubscribe.class);
            if(subscribeanno != null) {
                int subPort = subscribeanno.port();
                if(port == subPort) {

                   return subscribeMethod;

                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return null;
    }

}
