package com.xiangxue.webview.utils.progressbar;

public interface BaseProgressSpec {
    void show();

    void hide();

    void reset();

    void setProgress(int newProgress);
}
