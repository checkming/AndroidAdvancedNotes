package com.arch.demo.core.baseservices;

public abstract class ILoginListener {
    public abstract void onLogin();

    public void onPreLogout() {

    }

    public abstract void onLogout();

    public abstract void onRegister();

    public abstract void onCancel();
}
