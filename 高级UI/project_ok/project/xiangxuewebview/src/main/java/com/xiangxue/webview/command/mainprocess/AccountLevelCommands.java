package com.xiangxue.webview.command.mainprocess;

import com.xiangxue.webview.command.base.Commands;
import com.xiangxue.webview.utils.WebConstants;

public class AccountLevelCommands extends Commands {

    public AccountLevelCommands() {
    }

    @Override
    protected int getCommandLevel() {
        return WebConstants.LEVEL_ACCOUNT;
    }

}
