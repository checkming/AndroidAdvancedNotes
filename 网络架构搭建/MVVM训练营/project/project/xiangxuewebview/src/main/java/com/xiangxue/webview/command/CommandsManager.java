package com.xiangxue.webview.command;

import android.content.Context;
import android.util.Log;

import com.xiangxue.webview.utils.AidlError;
import com.xiangxue.webview.utils.WebConstants;

import java.util.HashMap;
import java.util.Map;
public class CommandsManager {
    private static final String TAG = "CommandsManager";
    private static CommandsManager instance;

    private HashMap<String, Command> commands;

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public CommandsManager() {
        commands = new HashMap<>();
    }

    public void registerCommand(Command command) {
        commands.put(command.name(), command);
    }

    public static CommandsManager getInstance() {
        if (instance == null) {
            synchronized (CommandsManager.class) {
                instance = new CommandsManager();
            }
        }
        Log.d(TAG, instance + "");
        return instance;
    }

    /**
     * 非UI Command 的执行
     */
    public void execMainProcessCommand(Context context, String action, Map params, ResultBack resultBack) {
        if (getCommands().get(action) != null) {
            getCommands().get(action).exec(context, params, resultBack);
        } else {
            AidlError aidlError = new AidlError(WebConstants.ERRORCODE.NO_METHOD, WebConstants.ERRORMESSAGE.NO_METHOD);
            resultBack.onResult(WebConstants.FAILED, action, aidlError);
        }
    }

    /**
     * CommandsManager handled by Webview itself, these command does not require aidl.
     */
    public void execWebviewProcessCommand(Context context, String action, Map params, ResultBack resultBack) {
        if (getCommands().get(action) != null) {
            getCommands().get(action).exec(context, params, resultBack);
        }
    }

    public boolean isWebviewProcessCommand(String action) {
        return getCommands().get(action) != null;
    }
}


