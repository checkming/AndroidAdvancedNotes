package com.xiangxue.webview.command;

import com.xiangxue.webview.ICallbackFromMainToWeb;

import java.util.Map;

public interface Command {
    String name();
    void exec(Map params, ICallbackFromMainToWeb resultBack);
}
