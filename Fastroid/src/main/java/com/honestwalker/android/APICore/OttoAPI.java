package com.honestwalker.android.APICore;

import android.content.Context;

import com.honestwalker.android.APICore.API.APIListener;
import com.honestwalker.android.APICore.API.BaseAPI;

/**
 * Created by lanzhe on 16-11-14.
 */
public class OttoAPI extends BaseAPI {

    public OttoAPI(Context context) {
        super(context);
    }

    public void request(final Object req,final Class respClass,final APIListener apiListener) {
        request(req, apiListener, respClass);
    }

}
