package com.xiangxue.common.router;

public final class CommonRouter {
    private static final String COMMON = "/common/";

    private CommonRouter(){}

    /**
     * Common webview activity
     * url is necessary parameter.
     */
    public static final String COMMON_WEB_VIEW_ACTIVITY = COMMON + "webview_activity";
    public final static String AUTO_WIRED_PARAM_URL = "url";

}

