package com.honestwalker.android.kc_test;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import com.honestwalker.android.kc_test.models.Action;
import com.honestwalker.android.kc_test.models.Actions;
import com.honestwalker.android.kc_test.models.Task;
import com.honestwalker.android.kc_test.models.TesterConfigReader;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.RClassUtil;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.UIHandler;
import com.honestwalker.androidutils.exception.ExceptionUtil;
import com.honestwalker.androidutils.pool.ThreadPool;

import java.util.Map;
import java.util.Queue;

/**
 * Created by lanzhe on 16-10-28.
 */
public class KCTestLauncher {

    private Actions actions;

    private Activity context;

    private static Class rClass;

    private static int testerConfig;

    private static String nextAction;

    public static void init (Class rClass, int testerConfig) {
        KCTestLauncher.rClass = rClass;
        KCTestLauncher.testerConfig = testerConfig;
    }

    public void start(Activity context) {
        this.context = context;
        TesterConfigReader reader = new TesterConfigReader();
        try {
            actions = reader.load(context, testerConfig);
        } catch (Exception e) {
            ExceptionUtil.showException(e);
        }
        execute();
    }

    public void next(Activity context) {

        if(StringUtil.isEmptyOrNull(KCTestLauncher.nextAction)) return;

        this.context = context;
        execute();
        KCTestLauncher.nextAction = "";
    }

    private void inputAction(final Task task) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                int viewResId = RClassUtil.getResId(rClass, "id." + task.getDesc());
                EditText view = (EditText) context.findViewById(viewResId);
                view.setText(task.getValue());
            }
        });
    }

    private void goBack() {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                context.onBackPressed();
            }
        });
    }

    private void clickAction(final Task task) {

        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                int viewResId = RClassUtil.getResId(rClass, "id." + task.getDesc());
                Button view = (Button) context.findViewById(viewResId);
                view.performClick();

                // test
                int[] xy = new int[2];
                view.getLocationOnScreen(xy);
                LogCat.d("tester", xy[0] + ":" + xy[1]);
            }
        });

//        WindowManager windowManager = getWindowManager();
//        View v = new View(this);
////                    ViewSizeHelper.getInstance(this).marginLeft(v, xy[0]);
////                    ViewSizeHelper.getInstance(this).marginTop(v, xy[1]);
////                    ViewSizeHelper.getInstance(this).setWidth(v, 15);
////                    ViewSizeHelper.getInstance(this).setHeight(v, 15);
//        v.setBackgroundColor(getResources().getColor(R.color.blue));
////                    lp.width = 15;
////                    lp.height = 15;
////                    v.setLayoutParams(lp);
////                    WindowManager.LayoutParams wlp = new WindowManager.LayoutParams(15,15 , WindowManager.LayoutParams.TYPE_APPLICATION, 0x00000080, PixelFormat.OPAQUE);
//        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
//        mParams.format = PixelFormat.TRANSLUCENT;
//        mParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        mParams.width = 15;
//        mParams.height = 15;
//        mParams.x = 0;
//        mParams.y = 0;
//        windowManager.addView(v, mParams);
    }

    private void delayAction(Task task) {
        ThreadPool.sleep(Long.parseLong(task.getValue()));
    }

    private void execute() {

        Map<String, Action> actionMap = actions.getActions();

        String packageName = context.getClass().getName();
        LogCat.d("tester", "进入 " + packageName);

        Action action = null;

        if(!StringUtil.isEmptyOrNull(nextAction)) {
            action = actionMap.get(nextAction);
        } else {
            action = actionMap.get(packageName);
        }

        if(action == null) return;

        LogCat.d("tester", "执行 " + packageName + "的action  " + action.getActid());

        final Queue<Task> taskQueue = action.getTasks();
        ThreadPool.threadBackgroundPool(new Runnable() {
            @Override
            public void run() {
                for (Task task : taskQueue) {
                    String event = task.getEvent();
                    if("input".equals(event)) {
                        inputAction(task);
                    } else if("click".equals(event)) {
                        clickAction(task);
                    } else if("delay".equals(event)) {
                        delayAction(task);
                    } else if("goBack".equals(event)) {
                        KCTestLauncher.nextAction = task.getNextAction();
                        goBack();
                    }
                }
            }
        });

    }

}
