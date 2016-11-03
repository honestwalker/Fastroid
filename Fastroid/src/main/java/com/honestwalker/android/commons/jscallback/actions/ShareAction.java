package com.honestwalker.android.commons.jscallback.actions;

import android.app.Activity;

import com.honestwalker.android.commons.jscallback.bean.ShareParamBean;
import com.honestwalker.android.commons.views.HtmlWebView.HtmlWebView;
import com.honestwalker.android.fastroid.R;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.SharedPreferencesLoader;
import com.honestwalker.androidutils.UIHandler;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * share js callback 业务实现
 * Created by honestwalker on 15-6-2.
 */
public class ShareAction extends JSCallbackAction<ShareParamBean> {

    private Activity context;
    private OnekeyShare oks;
    private ShareParamBean paramBean;
    private HtmlWebView webView;

    @Override
    public String doAction(Activity context , ShareParamBean paramBean , HtmlWebView webView) {
        this.context = context;
        this.paramBean = paramBean;
        this.webView = webView;
        LogCat.d("share" , "paramBean.getUrl()=" + paramBean.getUrl());
        oks = new OnekeyShare();
        oks.setShareContentCustomizeCallback(shareContentCustomizeCallback);   //修改分享朋友圈时修改标题为内容
        oks.setImageUrl(paramBean.getImg());	//设置分享内容中图片到链接
        oks.setUrl(paramBean.getUrl());	//设置点击微信分享内容需要跳转到到url
        oks.setComment(context.getString(R.string.share));	//设置人人网中对分享到评论
        oks.setSite(context.getString(R.string.app_name));	//设置分享该内容的站点
        oks.setSiteUrl(paramBean.getUrl());	//设置分享该内容站点到url
//        oks.setSiteUrl("http://www.baidu.com");	//设置分享该内容站点到url
        oks.setSilent(false);	//设置是否直接分享
        oks.setShareFromQQAuthSupport(false);	//是否支持QQ,QZone授权登录后发微博
        oks.setTheme(OnekeyShareTheme.CLASSIC);	//使用经典到分享界面
        oks.setCallback(platformActionListener);
        // 令编辑页面显示为Dialog模式
        oks.setDialogMode();
        oks.show(context);
        return "";
    }

    private ShareContentCustomizeCallback shareContentCustomizeCallback = new ShareContentCustomizeCallback() {
        @Override
        public void onShare(Platform platform, Platform.ShareParams paramsToShare) {

            // 延时执行
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferencesLoader.putBoolean("slot-machine-shared" , true);
                }
            } , 3000);

            if("WechatMoments".equals(platform.getName())){
                LogCat.d("share","分享到朋友圈");
                paramsToShare.setTitle(paramBean.getContent());
                paramsToShare.setText(paramBean.getTitle());
            }else if("Wechat".equals(platform.getName())){
                LogCat.d("share","分享到朋友");
                paramsToShare.setTitle(paramBean.getTitle());
                paramsToShare.setText(paramBean.getContent());
            }

        }
    };

    private PlatformActionListener platformActionListener = new PlatformActionListener() {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
            UIHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    webView.loadUrl("javascript:weixinShareStats(" + CacheManager.userInfo.get(context).getCustomer().getCustomer_id() + ", \"" + paramBean.getUrl() + "\")");
                    LogCat.d("share", "分享成功 " + webView.getUrl());
                }
            }, 3000);

        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            LogCat.d("share","分享失败:"+throwable.toString());
        }

        @Override
        public void onCancel(Platform platform, int i) {
        }
    };

}
