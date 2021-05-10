package com.xiangxue.webview.mainprocess;

import android.content.Context;

import com.google.gson.Gson;
import com.xiangxue.webview.ICallbackFromMainToWeb;
import com.xiangxue.webview.IWebToMain;
import com.xiangxue.webview.command.CommandsManager;
import com.xiangxue.webview.command.ResultBack;

import java.util.Map;

public class MainProAidlInterface extends IWebToMain.Stub {

    private Context context;
    public MainProAidlInterface(Context context) {
        this.context = context;
    }

    @Override
    public void handleWebAction(final String actionName, String jsonParams, final ICallbackFromMainToWeb callback) {
        CommandsManager.getInstance().execMainProcessCommand(context, actionName, new Gson().fromJson(jsonParams, Map.class), new ResultBack() {
            @Override
            public void onResult(int status, String action, Object result) {
                try {
                    if (callback != null) {
                        callback.onResult(status, actionName, new Gson().toJson(result));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
