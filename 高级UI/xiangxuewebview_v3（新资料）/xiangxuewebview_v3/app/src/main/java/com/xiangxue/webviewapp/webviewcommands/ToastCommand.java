package com.xiangxue.webviewapp.webviewcommands;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.arch.demo.core.BaseApplication;
import com.xiangxue.webview.ICallbackFromMainToWeb;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.mainprocess.CommandsManager;

import java.util.Map;

public class ToastCommand implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    public static final void init() {
        CommandsManager.getsInstance().registerCommand(new ToastCommand());
    }
    @Override
    public void exec(final Map params, ICallbackFromMainToWeb resultBack) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseApplication.sApplication, String.valueOf(params.get("message")), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
