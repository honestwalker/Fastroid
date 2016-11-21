package com.honestwalker.android.kc_test.actions;

import com.honestwalker.android.otto.BusManager;
import com.honestwalker.androidutils.IO.LogCat;

/**
 * Created by lanzhe on 16-11-4.
 */
public abstract class KCTestBaseAction {

    protected KCTestBaseAction() {
        registerBusMessage();
    }

    protected void registerBusMessage() {
        LogCat.d("tester", "注册 " + this.getClass().getSimpleName());
        BusManager.register(this);
    }

}
