package com.xiangxue.network.errorhandler;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by Allen on 2017/7/20.
 * 保留所有版权，未经允许请不要分享到互联网和其他人
 */

/**
 * HttpResponseFunc处理以下两类网络错误：
 * 1、http请求相关的错误，例如：404，403，socket timeout等等；
 * 2、应用数据的错误会抛RuntimeException，最后也会走到这个函数来统一处理；
 */
public class HttpErrorHandler<T> implements Function<Throwable, Observable<T>> {
    @Override
    public io.reactivex.Observable<T> apply(Throwable throwable) throws Exception {
        return io.reactivex.Observable.error(ExceptionHandle.handleException(throwable));
    }
}
