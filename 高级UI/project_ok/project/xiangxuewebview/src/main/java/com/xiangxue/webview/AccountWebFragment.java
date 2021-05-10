package com.xiangxue.webview;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;

import com.xiangxue.webview.basefragment.BaseWebviewFragment;
import com.xiangxue.webview.utils.WebConstants;
import java.util.HashMap;
import java.util.Map;

public class AccountWebFragment extends BaseWebviewFragment {
    public static AccountWebFragment newInstance(@NonNull String keyUrl, @NonNull HashMap<String, String> headers, boolean isSyncToCookie) {
        AccountWebFragment fragment = new AccountWebFragment();
        fragment.setArguments(getBundle(keyUrl, headers));
        if(isSyncToCookie && headers != null) {
            syncCookie(keyUrl, (headers));
        }
        return fragment;
    }

    public static Bundle getBundle(@NonNull String url, @NonNull HashMap<String, String> headers) {
        Bundle bundle = new Bundle();
        bundle.putString(WebConstants.INTENT_TAG_URL, url);
        bundle.putSerializable(ACCOUNT_INFO_HEADERS, headers);
        return bundle;
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public static boolean syncCookie(String url, Map<String, String> map) {
        CookieManager cookieManager = CookieManager.getInstance();
        for(String key:map.keySet()){
            cookieManager.setCookie(url, key+"="+map.get(key));
        }
        String newCookie = cookieManager.getCookie(url);
        return TextUtils.isEmpty(newCookie) ? false : true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_common_webview;
    }

    @Override
    public int getCommandLevel() {
        return WebConstants.LEVEL_ACCOUNT;
    }
}
