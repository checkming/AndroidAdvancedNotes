package com.xiangxue.webview.basefragment.commands;

import android.content.Context;
import android.widget.Toast;

import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.command.ResultBack;

import java.util.Map;

public class ToastCommand implements Command {
    @Override
    public String name() {
        return "showToast";
    }

    @Override
    public void exec(Context context, Map params, ResultBack resultBack) {
        Toast.makeText(context, String.valueOf(params.get("message")), Toast.LENGTH_SHORT).show();
    }
}
