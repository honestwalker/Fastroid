package com.honestwalker.android.APICore;

/**
 * Created by lanzhe on 16-11-14.
 */
public @interface APISubscribe {

    /**
     * 接受者端口，也是接受方法的识别身份
     * @return subscribe的端口号
     */
    int port();

//    Class<? extends BaseResp> resp();

}
