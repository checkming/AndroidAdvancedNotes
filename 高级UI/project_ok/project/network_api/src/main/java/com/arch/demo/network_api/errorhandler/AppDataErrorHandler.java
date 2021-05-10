package com.arch.demo.network_api.errorhandler;

import com.arch.demo.network_api.beans.BaseResponse;

import io.reactivex.functions.Function;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */
/**
 * HandleFuc处理以下网络错误：
 * 1、应用数据的错误会抛RuntimeException；
 */
public class AppDataErrorHandler implements Function<BaseResponse,BaseResponse> {
    @Override
    public BaseResponse apply(BaseResponse response) throws Exception {
        //response中code码不会0 出现错误
        if (response instanceof BaseResponse && response.showapiResCode != 0)
            throw new RuntimeException(response.showapiResCode + "" + (response.showapiResError != null ? response.showapiResError : ""));
        return response;
    }
}