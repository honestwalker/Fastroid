package com.honestwalker.android.share;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * ShareSDK分享工具类
 * Created by lanzhe on 16-8-4.
 */
public class ShareUtil {

    public void share(String platform , Platform.ShareParams shareParams, PlatformActionListener listener) {
        Platform mPlatform = ShareSDK.getPlatform (Wechat.NAME);
        if(platform == null) return;
        mPlatform. setPlatformActionListener (listener);
        mPlatform.share(shareParams);
    }

}
