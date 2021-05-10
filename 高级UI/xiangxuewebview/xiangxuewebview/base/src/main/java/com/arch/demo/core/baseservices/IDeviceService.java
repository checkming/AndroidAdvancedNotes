package com.arch.demo.core.baseservices;


/**
 * 描述:  与设备相关的配置信息
 *
 * @author neifang
 * @date 2017/8/21 14:58
 */

public interface IDeviceService {
    String DEVICE_SERVICE_NAME = "device_service";

    String getModel();

    /***
     * 获取MCC
     */
    public String getMcc();


    /***
     * 获取Operator MCC
     */
    public String getNetworkMcc();

    /***
     * 获取Mnc
     */
    public String getMnc();

    /***
     * 获取Operator MCC
     */
    public String getNetworkMnc();

    /**
     * 获取设备默认语言
     */
    public String getDeviceDefaultLanguage();

    /**
     * 获取ClientId
     *
     * @return
     */
    public String getClientId();

    /**
     * 获取老版本ClientId
     *
     * @return
     */
    public int getOldVersionCode();

    /**
     * 获取当前设备安卓系统版本号
     */
    public String getSystemVersion();

    /**
     * 获取手机品牌
     *
     * @return
     */
    public String getPhoneBrand();

    /**
     * 获取手机Android API等级（22、23 ...）
     *
     * @return
     */
    public int getBuildLevel();

    /**
     * 获取设备宽度（px）
     *
     * @return
     */
    public int getDeviceWidth();

    /**
     * 获取设备高度（px）
     *
     * @return
     */
    public int getDeviceHeight();

    /**
     * SD卡判断
     *
     * @return
     */
    public boolean isSDCardAvailable();

    /**
     * 是否有网
     *
     * @return
     */
    public boolean isNetworkConnected();

    /**
     * 是否这次安装时新安装
     */
    boolean isNewInstall();

    /**
     * 是否是三星设备
     * @return
     */
    public boolean isSamSungDevice();
}
