package com.xiangxue.webview.remotewebview.progressbar;

public interface BaseProgressSpec {
    void show();

    void hide();

    void reset();

    void setProgress(int newProgress);
}
