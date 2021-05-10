package com.xiangxue.webview.command.mainprocess;

import com.xiangxue.webview.command.base.Commands;
import com.xiangxue.webview.utils.WebConstants;

public class BaseLevelCommands extends Commands {

    public BaseLevelCommands() {
    }

    @Override
    protected int getCommandLevel() {
        return WebConstants.LEVEL_BASE;
    }
}
