package com.xiangxue.webview.command;

import com.xiangxue.webview.utils.WebConstants;

public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
    }

    @Override
    int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
