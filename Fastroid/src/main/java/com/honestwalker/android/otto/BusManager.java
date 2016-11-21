package com.honestwalker.android.otto;

import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.UIHandler;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by lanzhe on 16-11-11.
 */
public class BusManager {

    private static Bus busInstance;

    public static Bus init() {

        if(busInstance == null) {
            busInstance = new Bus(ThreadEnforcer.ANY);
        }

        return busInstance;

    }

    /**
     * 获得bus
     * @return
     */
    public static Bus getBus() {
        return busInstance;
    }

    /**
     * 发送消息
     * @param data
     */
    public static void post(final Object data) {
        if(data == null) return;
        LogCat.d("otto", "发送消息 " + data.toString());
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                busInstance.post(data);
            }
        });
    }

    /**
     * 注册消息接收
     * @param data
     */
    public static void register(Object data) {
        if(data == null) return;
        busInstance.register(data);
    }

    /**
     * 反注册消息接收
     * @param data
     */
    public static void unregister(Object data) {
        busInstance.unregister(data);
    }

}
