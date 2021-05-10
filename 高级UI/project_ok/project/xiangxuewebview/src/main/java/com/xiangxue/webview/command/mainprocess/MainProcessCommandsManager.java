package com.xiangxue.webview.command.mainprocess;

import android.content.Context;
import android.util.Log;

import com.xiangxue.webview.command.base.Command;
import com.xiangxue.webview.command.base.ResultBack;
import com.xiangxue.webview.utils.AidlError;
import com.xiangxue.webview.utils.WebConstants;

import java.util.Map;

public class MainProcessCommandsManager {

    private static MainProcessCommandsManager instance;

    private BaseLevelCommands baseLevelCommands;
    private AccountLevelCommands accountLevelCommands;

    private MainProcessCommandsManager() {
        baseLevelCommands = new BaseLevelCommands();
        accountLevelCommands = new AccountLevelCommands();
    }

    public static MainProcessCommandsManager getInstance() {
        if (instance == null) {
                   synchronized (MainProcessCommandsManager.class) {

                instance = new MainProcessCommandsManager();
            }
        }
        Log.d("WebviewProcessCommandsManager:", instance + "");
        return instance;
    }

    /**
     * 动态注册command
     * 应用场景：其他模块在使用WebView的时候，需要增加特定的command，动态加进来
     */
    public void registerCommand(int commandLevel, Command command) {
        switch (commandLevel) {
            case WebConstants.LEVEL_BASE:
                baseLevelCommands.registerCommand(command);
                break;
            case WebConstants.LEVEL_ACCOUNT:
                accountLevelCommands.registerCommand(command);
                break;
        }
    }

    /**
     * 非UI Command 的执行
     */
    public void findAndExecRemoteCommand(Context context, int level, String action, Map params, ResultBack resultBack) {
        boolean methodFlag = false;
        switch (level) {
            case WebConstants.LEVEL_BASE: {
                if (baseLevelCommands.getCommands().get(action) != null) {
                    methodFlag = true;
                    baseLevelCommands.getCommands().get(action).exec(context, params, resultBack);
                }
                if (accountLevelCommands.getCommands().get(action) != null) {
                    AidlError aidlError = new AidlError(WebConstants.ERRORCODE.NO_AUTH, WebConstants.ERRORMESSAGE.NO_AUTH);
                    resultBack.onResult(WebConstants.FAILED, action, aidlError);
                }
                break;
            }
            case WebConstants.LEVEL_ACCOUNT: {
                if (baseLevelCommands.getCommands().get(action) != null) {
                    methodFlag = true;
                    baseLevelCommands.getCommands().get(action).exec(context, params, resultBack);
                }
                if (accountLevelCommands.getCommands().get(action) != null) {
                    methodFlag = true;
                    accountLevelCommands.getCommands().get(action).exec(context, params, resultBack);
                }
                break;
            }
        }
        if (!methodFlag) {
            AidlError aidlError = new AidlError(WebConstants.ERRORCODE.NO_METHOD, WebConstants.ERRORMESSAGE.NO_METHOD);
            resultBack.onResult(WebConstants.FAILED, action, aidlError);
        }
    }
}

