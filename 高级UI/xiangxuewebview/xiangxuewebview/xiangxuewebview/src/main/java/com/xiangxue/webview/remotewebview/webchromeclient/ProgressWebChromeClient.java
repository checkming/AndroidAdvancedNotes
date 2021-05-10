package com.xiangxue.webview.remotewebview.webchromeclient;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.command.ResultBack;
import com.xiangxue.webview.remotewebview.BaseWebView;
import com.xiangxue.webview.remotewebview.ProgressWebView;
import com.xiangxue.webview.utils.WebConstants;

import java.util.HashMap;
import java.util.Map;

import static com.xiangxue.webview.command.Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE;

public class ProgressWebChromeClient extends WebChromeClient {

    private Handler progressHandler;

    public ProgressWebChromeClient(Handler progressHandler) {
        this.progressHandler = progressHandler;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        if(view instanceof ProgressWebView) {
            if (!TextUtils.isEmpty(title)) {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(COMMAND_UPDATE_TITLE_PARAMS_TITLE, title);
                ((ProgressWebView)view).getWebViewCallBack().exec(view.getContext(), WebConstants.LEVEL_LOCAL, Command.COMMAND_UPDATE_TITLE, new Gson().toJson(params), view);
            }
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Message message = new Message();
        if (newProgress == 100) {
            message.obj = newProgress;
            progressHandler.sendMessageDelayed(message, 200);
        } else {
            if (newProgress < 10) {
                newProgress = 10;
            }
            message.obj = newProgress;
            progressHandler.sendMessage(message);
        }
        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsAlert(final WebView view, String url, String message, JsResult result) {
        new AlertDialog.Builder(view.getContext())
                .setTitle(android.R.string.dialog_alert_title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                //按钮事件
                                Toast.makeText(view.getContext(), view.getContext().getString(android.R.string.ok) + " clicked.", Toast.LENGTH_LONG).show();
                            }
                        }).show();
        //result.confirm();// 不加这行代码，会造成Alert劫持：Alert只会弹出一次，并且WebView会卡死
        return true;
    }
}
