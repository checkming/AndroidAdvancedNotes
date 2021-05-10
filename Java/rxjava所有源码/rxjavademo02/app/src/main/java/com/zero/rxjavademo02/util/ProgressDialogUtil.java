package com.zero.rxjavademo02.util;

import android.app.ProgressDialog;
import android.content.Context;

import java.lang.ref.WeakReference;


public class ProgressDialogUtil {

    private static ProgressDialog progress;
    private static WeakReference<Context> contextReference;

    public static void showProgress(Context context, String message) {
        showProgress(context);
        setMessage(message);
    }

    public static void setMessage(String message) {
        progress.setMessage(message);
    }

    public static void showProgress(Context context) {
        if (contextReference == null || contextReference.get() != context) {
            contextReference = new WeakReference<Context>(context);
            progress = new ProgressDialog(contextReference.get());
        }
        progress.setCanceledOnTouchOutside(false);
        if (!progress.isShowing()) {
            progress.show();
        }
    }

    public static void dismiss() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    public static boolean isShowing() {
        return progress.isShowing();
    }

    public static ProgressDialog getDialog(Context context, String content) {
        if (contextReference == null || contextReference.get() != context) {
            contextReference = new WeakReference<Context>(context);
            progress = new ProgressDialog(contextReference.get());
        }
        progress.setMessage(content);
        return progress;
    }
}
