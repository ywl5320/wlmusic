package com.ywl5320.wlmusic.http.service;




import com.ywl5320.wlmusic.http.httpentity.HttpResult;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by ywl on 2016/5/19.
 */
public class BaseApi {

    public <T> void toSubscribe(Observable<T> o, Observer<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     *           Func1(I,O) 输入和输出
     */
    public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

        @Override
        public T apply(HttpResult<T> httpResult) {
            if (httpResult.getStatus() == 200) {
                if(httpResult.getData() != null)
                {
                    return httpResult.getData();
                }
                throw new ExceptionApi(0x66666, httpResult.getMessage());
            }
            throw new ExceptionApi(httpResult.getStatus(), httpResult.getMessage());
        }
    }

}
