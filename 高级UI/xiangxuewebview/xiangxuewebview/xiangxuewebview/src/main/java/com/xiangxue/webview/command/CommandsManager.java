package com.xiangxue.webview.command;

import android.content.Context;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webview.utils.AidlError;
import java.util.Map;

public class CommandsManager {

    private static CommandsManager instance;

    private LocalCommands localCommands;
    private BaseLevelCommands baseLevelCommands;
    private AccountLevelCommands accountLevelCommands;

    private CommandsManager() {
        localCommands = new LocalCommands();
        baseLevelCommands = new BaseLevelCommands();
        accountLevelCommands = new AccountLevelCommands();
    }

    public static CommandsManager getInstance() {
        if (instance == null) {
            synchronized (CommandsManager.class) {
                instance = new CommandsManager();
            }
        }
        return instance;
    }

    /**
     * 动态注册command
     * 应用场景：其他模块在使用WebView的时候，需要增加特定的command，动态加进来
     */
    public void registerCommand(int commandLevel, Command command) {
        switch (commandLevel) {
            case WebConstants.LEVEL_LOCAL:
                localCommands.registerCommand(command);
                break;
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

    /**
     * Commands handled by Webview itself, these command does not require aidl.
     */
    public void findAndExecLocalCommnad(Context context, int level, String action, Map params, ResultBack resultBack) {
        if (localCommands.getCommands().get(action) != null) {
            localCommands.getCommands().get(action).exec(context, params, resultBack);
        }
    }

    public boolean checkHitLocalCommand(int level, String action) {
        return localCommands.getCommands().get(action) != null;
    }

}

