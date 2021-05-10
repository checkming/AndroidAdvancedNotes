package com.xiangxue.webview.command.webviewprocess;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.xiangxue.webview.command.base.Command;
import com.xiangxue.webview.command.base.Commands;
import com.xiangxue.webview.command.base.ResultBack;
import com.xiangxue.webview.utils.WebConstants;
import com.xiangxue.webview.utils.WebUtils;

import java.util.List;
import java.util.Map;

public class LocalCommands extends Commands {

    public LocalCommands() {
        super();
        registCommands();
    }

    @Override
    protected int getCommandLevel() {
        return WebConstants.LEVEL_LOCAL;
    }

    void registCommands() {
        registerCommand(showToastCommand);
        registerCommand(showDialogCommand);
    }

    /**
     * 显示Toast信息
     */
    private final Command showToastCommand = new Command() {
        @Override
        public String name() {
            return "showToast";
        }

        @Override
        public void exec(Context context, Map params, ResultBack resultBack) {
            Toast.makeText(context, String.valueOf(params.get("message")), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 对话框显示
     */
    private final Command showDialogCommand = new Command() {
        @Override
        public String name() {
            return "showDialog";
        }

        @Override
        public void exec(Context context, Map params, final ResultBack resultBack) {
            if (WebUtils.isNotNull(params)) {
                String title = (String) params.get("title");
                String content = (String) params.get("content");
                int canceledOutside = 1;
                if (params.get("canceledOutside") != null) {
                    canceledOutside = (int) (double) params.get("canceledOutside");
                }
                List<Map<String, String>> buttons = (List<Map<String, String>>) params.get("buttons");
                final String callbackName = (String) params.get(WebConstants.WEB2NATIVE_CALLBACk);
                if (!TextUtils.isEmpty(content)) {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(title)
                            .setMessage(content)
                            .create();
                    dialog.setCanceledOnTouchOutside(canceledOutside == 1 ? true : false);
                    if (WebUtils.isNotNull(buttons)) {
                        for (int i = 0; i < buttons.size(); i++) {
                           final Map<String, String> button = buttons.get(i);
                            int buttonWhich = getDialogButtonWhich(i);

                            if (buttonWhich == 0) return;

                            dialog.setButton(buttonWhich, button.get("title"), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    button.put(WebConstants.NATIVE2WEB_CALLBACK, callbackName);
                                    resultBack.onResult(WebConstants.SUCCESS, name(), button);
                                }
                            });
                        }
                    }
                    dialog.show();
                }
            }
        }

        private int getDialogButtonWhich(int index) {
            switch (index) {
                case 0:
                    return DialogInterface.BUTTON_POSITIVE;
                case 1:
                    return DialogInterface.BUTTON_NEGATIVE;
                case 2:
                    return DialogInterface.BUTTON_NEUTRAL;
            }
            return 0;
        }
    };
}
