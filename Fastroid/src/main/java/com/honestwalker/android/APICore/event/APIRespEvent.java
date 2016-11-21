package com.honestwalker.android.APICore.event;

import com.honestwalker.android.APICore.APISubscriber;

/**
 * Created by lanzhe on 16-11-14.
 */
public class APIRespEvent extends APIEvent {

    public APIRespEvent(APISubscriber apiSubscriber, Object data) {
        super(apiSubscriber, data);
    }

}
