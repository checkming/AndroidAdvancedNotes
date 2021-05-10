package com.arch.demo.core.preference;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
public class BasicDataPreferenceUtil extends BasePreferences {
    private static final String BASIC_DATA_PREFERENCE_FILE_NAME = "network_api_module_basic_data_preference";
    private static BasicDataPreferenceUtil sInstance;

    public static BasicDataPreferenceUtil getInstance() {
        if (sInstance == null) {
            synchronized (BasicDataPreferenceUtil.class) {
                if (sInstance == null) {
                    sInstance = new BasicDataPreferenceUtil();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected String getSPFileName() {
        return BASIC_DATA_PREFERENCE_FILE_NAME;
    }
}
