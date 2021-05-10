package com.arch.demo.common.router;

public final class AppRouter {
    private static final String APP = "/app/";

    private AppRouter() {

    }

    /**
     * Common webview activity
     * url is necessary parameter.
     */
    public static final String APP_LOGIN_ACTIVITY = APP + "login_activity";

    public static final String APP_TESTA_ACTIVITY = APP + "testa_activity";
    public static final String APP_TESTB_ACTIVITY = APP + "testb_activity";

}
