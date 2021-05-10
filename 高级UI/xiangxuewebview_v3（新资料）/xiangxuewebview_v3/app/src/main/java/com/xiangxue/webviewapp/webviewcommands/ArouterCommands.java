package com.xiangxue.webviewapp.webviewcommands;

import com.alibaba.android.arouter.launcher.ARouter;
import com.xiangxue.webview.ICallbackFromMainToWeb;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.mainprocess.CommandsManager;

import java.util.Map;

public final class ArouterCommands {

    private ArouterCommands() {
    }

    public static final void init() {
        CommandsManager.getsInstance().registerCommand(pageRouterCommand);
    }

    /**
     * 页面路由
     */
    private final static Command pageRouterCommand = new Command() {

        @Override
        public String name() {
            return "start_activity";
        }

        @Override
        public void exec(Map params, ICallbackFromMainToWeb resultBack) {
            ARouter.getInstance().build(params.get("arouter_navigation").toString()).navigation();
        }
    };
}
