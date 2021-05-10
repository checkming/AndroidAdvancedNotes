package com.arch.demo.core.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class ToastUtil {
	private static Toast mToast;

	public static void show(Context context, String msg) {
		try {
			if (context != null && !TextUtils.isEmpty(msg)) {
				if(mToast != null){
					mToast.cancel();
				}
				mToast = Toast.makeText(context, "", Toast.LENGTH_LONG);
				mToast.setText(msg);
				mToast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
