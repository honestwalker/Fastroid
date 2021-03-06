package com.honestwalker.android.kc_test.actions;

import android.app.Activity;

import com.honestwalker.android.kc_test.KCTestLauncher;
import com.honestwalker.android.kc_test.event.GoBackEvent;
import com.honestwalker.android.kc_test.models.Task;
import com.honestwalker.androidutils.UIHandler;
import com.squareup.otto.Subscribe;

/**
 * Created by lanzhe on 16-11-4.
 */
public class ActivityOperationAction extends KCTestBaseAction {

    @Subscribe
    public void goBack(final GoBackEvent event) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                KCTestLauncher.nextAction = event.getTask().getNextAction();

                KCTestLauncher.log("执行 " + event.getContext().getPackageName() + " 后退事件");

                event.getContext().onBackPressed();
            }
        });
    }

}
