package com.honestwalker.android.APICore.event;

import com.honestwalker.android.APICore.APISubscriber;

/**
 * API事件
 * Created by lanzhe on 16-11-15.
 */
public class APIEvent {

    /**
     * 事件数据
     */
    private Object data;

    /**
     * 消息事件订阅着
     */
    private APISubscriber apiSubscriber;

    public APIEvent(APISubscriber apiSubscriber, Object data) {
        this.data = data;
        this.apiSubscriber = apiSubscriber;
    }

    public Object getData() {
        return data;
    }

    public APISubscriber getAPISubscriber() {
        return apiSubscriber;
    }

}
