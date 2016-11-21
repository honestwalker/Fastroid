package com.honestwalker.android.kc_test.event;

import com.honestwalker.android.kc_test.actions.DelayAction;
import com.honestwalker.android.kc_test.actions.LogAction;
import com.honestwalker.android.otto.BusManager;
import com.honestwalker.androidutils.IO.LogCat;

/**
 * Created by lanzhe on 16-11-11.
 */
public class Events {

    private static String TAG = "tester";

    /**
     * 派发事件
     * @param eventData
     */
    public static void dispatchEvent(final EventData eventData) {
        String event = eventData.getTask().getEvent();
        LogCat.d(TAG, "开始派发事件:[" + eventData.getTask().getEvent() + "]");

        if      ("input"    .equals(event))    BusManager.post(new InputEvent(eventData));
        else if ("click"    .equals(event))    BusManager.post(new ClickEvent(eventData));
        else if ("longclick".equals(event))    BusManager.post(new LongClickEvent(eventData));
        else if ("delay"    .equals(event))    new DelayAction().delay(new DelayEvent(eventData));
        else if ("goBack"   .equals(event))    BusManager.post(new GoBackEvent(eventData));
        else if ("log"      .equals(event))    new LogAction().doAction(new LogEvent(eventData));
        else if ("alert"    .equals(event))    BusManager.post(new AlertEvent(eventData));
        else if ("toast"    .equals(event))    BusManager.post(new ToastEvent(eventData));
        else if ("method"   .equals(event))    {}

    }

}
