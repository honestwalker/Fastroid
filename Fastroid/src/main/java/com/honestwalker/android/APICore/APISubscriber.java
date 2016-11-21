package com.honestwalker.android.APICore;

/**
 * 消息事件订阅者
 * Created by lanzhe on 16-11-14.
 */
public class APISubscriber {

    /**
     * 请求成功回调端口，根据端口会找到回调的方法
     */
    private int successPort;
    /**
     * 请求失败回调端口，根据端口会找到回调的方法
     */
    private int failPort;

    /**
     * 接受回调的对象
     */
    private Object subscriber;

    public APISubscriber(Object subscriber, int successPort, int failPort) {
        this.successPort = successPort;
        this.failPort = failPort;
        this.subscriber = subscriber;
    }

    public int getSuccessPort() {
        return successPort;
    }

    public int getFailPort() {
        return failPort;
    }

    public Object getSubscriber() {
        return subscriber;
    }
}
