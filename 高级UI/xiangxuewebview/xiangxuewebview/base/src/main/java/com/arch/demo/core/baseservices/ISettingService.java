package com.arch.demo.core.baseservices;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述:
 *
 * @author neifang
 * @date 2017/8/19 11:12
 */

public interface ISettingService {
    String SETTINGS_SERVICE_NAME = "settings_service";

    /***
     *  不通知配置改变
     */
    public static final int CODE_NO_NOTIFY_CHANGE = -1;

    /**
     * 语言
     **/
    public static final int CODE_LANGUAGE = 1;

    /**
     * 主题
     */
    public static final int CODE_THEME = 2;

    /**
     * 持仓盈利
     */
    public static final int CODE_HOLDING_SHOW = 3;


    /**
     * 前台刷新平率
     */
    public static final int CODE_FOREGROUND_REFRESH_RATE = 4;

    /**
     * 字体
     */
    public static final int CODE_FONT_SCHEME = 5;

    /**
     * 涨跌幅颜色
     */
    public static final int CODE_COLOR_SCHEME = 6;

    /**
     * 股票显示方式
     */
    public static final int CODE_NAME_SYMBOL = 7;

    /**
     * 默认图标方式
     */
    public static final int CODE_DEFAULT_CHART = 8;

    /**
     * 默认排序
     */
    public static final int CODE_ORDER_BY = 9;

    /**
     * 组合列表样式
     */
    public static final int CODE_PORTFOLIO_LISTING = 10;

    /**
     * 是否显示通知
     **/
    public static final int CODE_NOTIFICATION_ENABLE = 11;

    /**
     * 提醒策略
     **/
    public static final int CODE_NOTIFICATION_PERIOD = 12;

    /**
     * 谷歌同步
     */
    public static final int CODE_GOOGLE_FINANCE_SYNC = 13;

    /**
     * 地区改变
     */
    public static final int CODE_MARKET_REGION = 14;

    /**
     * 谷歌TOKEN刷新
     */
    public static final int CODE_GOOGLE_TOKEN_REFRESH = 15;
    /**
     * 自动切换分时图
     */
    public static final int CODE_AUTO_SWITCH_DAILY_CHART = 16;

    /**
     * 在浏览器中打开新闻
     */
    public static final int CODE_NEWS_OPEN_BROWSER = 17;

    /**
     * 是否打开组合总览
     */
    public static final int CODE_ENABLE_GENERAL = 18;

    public static final int FONT_SCHEME_SMALL = 0;
    public static final int FONT_SCHEME_STANDARD = 1;
    public static final int FONT_SCHEME_MEDIUM = 2;
    public static final int FONT_SCHEME_LARGE = 3;
    public static final int FONT_SCHEME_EX_LARGE = 4;


    @IntDef({CODE_NO_NOTIFY_CHANGE, CODE_LANGUAGE, CODE_THEME, CODE_HOLDING_SHOW, CODE_FOREGROUND_REFRESH_RATE, CODE_FONT_SCHEME, CODE_COLOR_SCHEME,
            CODE_NAME_SYMBOL, CODE_DEFAULT_CHART, CODE_ORDER_BY, CODE_PORTFOLIO_LISTING, CODE_NOTIFICATION_ENABLE, CODE_NOTIFICATION_PERIOD, CODE_GOOGLE_FINANCE_SYNC,
            CODE_MARKET_REGION, CODE_GOOGLE_TOKEN_REFRESH, CODE_AUTO_SWITCH_DAILY_CHART, CODE_NEWS_OPEN_BROWSER, CODE_ENABLE_GENERAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SettingCode {

    }


    /***
     *  注册配置信息改变
     * @param code
     * @param changeListener
     */
    public void registerListener(int code, ISettingService.OnSettingPreferenceChangeListener changeListener);

    /***
     * 获取语言
     * @return
     */
    public String getLanguage();

    /**
     * 设置语言
     */
    public void setLanguage(String language);


    /***
     * 获取主题
     * @return 0:暗色  1.亮色  2.纯黑
     */
    public int getThemeValue();

    public void setThemeValue(String theme);


    //开启智能换肤
    public boolean isOpenChangeOnTime();


    /***
     * 获取字体
     * @return 0:大  1.中  2.小
     */
    public int getFontSchemeValue();

    public int getNewFontSchemeValue();
    /***
     * 获取配色方案
     * @return 0:涨：绿色，跌：红色  1：涨：红色，跌：绿色  2：没有颜色 3： 涨：绿色，跌：黄色
     */
    public int getColorSchemeValue();

    /***
     * 获取股票显示方式
     * @return false:代码，名称  true：名称，代码
     */
    public boolean isNameSymbol();

    /***
     * 获取股票显示方式
     * @return 0:代码，名称  1：名称，代码
     */
    public String getNameSymbol();


    /***
     * 获取默认图表
     * @return
     *  英文： 1d:One Day(Default)，  5d:Five Days， 3M：Three Months，  1Y:One Year， 5Y：Five Years  40Y：Max
     *  简体：1d:1日 ,5d：5日, Daily:日K, Weekily:周K,  Monthily:月K
     */
    public String getDefaultChart();


    /***
     * 获取默认排序
     * @return 0:拖动排序(默认), 1：名称, 2:代码, 3:涨幅, 4:跌幅 5:振幅
     */
    public int getOrderValue();


    /***
     * 获取默认排序
     * @return 1:优先展示涨跌幅, 2：同时展示涨跌幅与涨跌额
     */
    public int getPortfolioStyleValue();

    /**
     * 获取刷新频率
     *
     * @return 2： 表示手动刷新  1：表示实时推送  5000:5秒  10000: 10秒  20000:20秒  30000：30秒  60000：60秒
     */
    public int getForegroundRefreshRate();

    /**
     * 是否启动谷歌财经同步
     *
     * @return
     */
    public boolean isGoogleFinanceSyncEnable();

    /**
     * 获取谷歌账号
     */
    public String getGoogleAccount();

    /***
     *  是否在浏览器中打开新闻
     * @return
     */
    public boolean isOpenNewsByBrowserEnable();

    /**
     * 获取TOKEN
     */
    public String getGoogleToken();

    /**
     * 初始化对Google的接口监听
     */
    public void initGoolgeFinanceExpireListener();

    /**
     * 刷新谷歌同步
     */
    public void refreshGoogleToken();

    /**
     * 是否显示持仓
     *
     * @return
     */
    public boolean isShowHolding();

    /***
     *  是否显示通知
     * @return
     */
    public boolean isNotificationEnable();


    /***
     *  是否启动通知振动
     * @return
     */
    public boolean isNotificationVibrateEnable();


    /***
     * 获取铃声
     * @return
     */
    public String getNotificationRingtone();

    /**
     * 获取提醒策略
     *
     * @return
     */
    public int getNotificationPeriodValue();

    /***
     * 获取提醒开始时间
     * @return
     */
    public String getNotificationStartTime();

    /***
     * 获取提醒结束时间
     * @return
     */
    public String getNotificationEndTime();

    /**
     * 用户同步设置
     */
    public void syncUserSetting();

    /***
     * 用户上传设置
     */
    public void updateUserSetting();

    /**
     * 是自动切换分时图
     */
    boolean isAutoSwitchDailyChart();

    void setAutoSwitchDailyChart(boolean isOpen);

    interface OnSettingPreferenceChangeListener {
        public void onPreferenceChange(int code);
    }

    public void initTextFontWithSystem(float fontScale);


    public boolean isOpenNightTheme();

    public void setOpenNightTheme(boolean openNightTheme);

    public int getNightTheme();

    public String getNightThemeToString();

    public boolean isUserCloseTheme();

    public boolean isActivityNeedRestart(int oldTheme);

    public boolean isAutoOrManually();

    public void setAutoOrManually(boolean autoOrManually);

    public void setUserCloseTheme(boolean userCloseTheme);

    public void setManualEnableGeneral(boolean enableGeneral);

    public void setAutoEnableGeneral(boolean enableGeneral);

    public boolean isEnableGeneral();

    public int getDefaultShowPortfolioId();

    public void saveDefaultShowPortfolioId(int portfolioId);

    public int getEnvironmentId();

}
