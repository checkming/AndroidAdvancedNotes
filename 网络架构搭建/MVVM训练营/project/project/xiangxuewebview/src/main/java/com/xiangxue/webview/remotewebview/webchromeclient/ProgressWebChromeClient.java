package com.xiangxue.webview.remotewebview.webchromeclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.xiangxue.webview.command.Command;
import com.xiangxue.webview.remotewebview.BaseWebView;
import com.xiangxue.webview.remotewebview.ProgressWebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.xiangxue.webview.command.Command.COMMAND_UPDATE_TITLE_PARAMS_TITLE;

public class ProgressWebChromeClient extends WebChromeClient {

    private ValueCallback<Uri[]> mFilePathCallback;
    private Handler progressHandler;
    private String mCameraPhotoPath;

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
                ((BaseWebView) view).post(Command.COMMAND_UPDATE_TITLE, new Gson().toJson(params));
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


    //For Android5.0+
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(webView.getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }
        ((BaseWebView)webView).getWebViewCallBack().onShowFileChooser(takePictureIntent, mFilePathCallback);
        return true;
    }

    /**
     * More info this method can be found at
     * http://developer.android.com/training/camera/photobasics.html
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        return imageFile;
    }
}
