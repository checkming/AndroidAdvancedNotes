package com.xiangxue.webview.command;

import com.xiangxue.webview.utils.WebConstants;

public class AccountLevelCommands extends Commands {

    public AccountLevelCommands() {
    }

    @Override
    int getCommandLevel() {
        return WebConstants.LEVEL_ACCOUNT;
    }

}
