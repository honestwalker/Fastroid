package com.honestwalker.android.kc_test.event;

import com.squareup.otto.Subscribe;

/**
 * Created by lanzhe on 16-11-11.
 */
public class GoBackEvent extends EventData {

    public GoBackEvent(){}

    public GoBackEvent(EventData eventData){
        super(eventData);
    }

}
