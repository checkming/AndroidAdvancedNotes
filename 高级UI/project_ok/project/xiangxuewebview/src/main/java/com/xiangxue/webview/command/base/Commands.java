package com.xiangxue.webview.command.base;

import java.util.HashMap;

public abstract class Commands {

    private HashMap<String, Command> commands;

    protected abstract int getCommandLevel();

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public Commands() {
        commands = new HashMap<>();
    }

    public void registerCommand(Command command) {
        commands.put(command.name(), command);
    }
}


