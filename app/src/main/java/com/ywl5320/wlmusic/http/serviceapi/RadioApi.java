package com.ywl5320.wlmusic.http.serviceapi;

import com.ywl5320.wlmusic.beans.ChannelSchedulBean;
import com.ywl5320.wlmusic.beans.HomePageBean;
import com.ywl5320.wlmusic.beans.LiveChannelBean;
import com.ywl5320.wlmusic.beans.LivePageBean;
import com.ywl5320.wlmusic.beans.PlaceBean;
import com.ywl5320.wlmusic.http.service.BaseApi;
import com.ywl5320.wlmusic.http.service.HttpMethod;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Created by ywl on 2016/5/19.
 */
public class RadioApi extends BaseApi {

    public static RadioApi radioApi;
    public RadioService radioService;
    public RadioApi()
    {
        radioService = HttpMethod.getInstance().createApi(RadioService.class);
    }

    public static RadioApi getInstance()
    {
        if(radioApi == null)
        {
            radioApi = new RadioApi();
        }
        return radioApi;
    }
    /*-------------------------------------获取的方法-------------------------------------*/

    public void getToken(Observer<Long> subscriber)
    {
        Observable observable = radioService.getToken()
                .map(new HttpResultFunc<Long>());

        toSubscribe(observable, subscriber);
    }

    public void getHomePage(String token, Observer<HomePageBean> subscriber)
    {
        Observable observable = radioService.getHomePage(token)
                .map(new HttpResultFunc<HomePageBean>());

        toSubscribe(observable, subscriber);
    }

    public void getLivePage(String token, Observer<LivePageBean> subscriber)
    {
        Observable observable = radioService.getLivePage(token)
                .map(new HttpResultFunc<LivePageBean>());

        toSubscribe(observable, subscriber);
    }

    public void getLiveByParam(String token, String channelPlaceId, int limit, int offset, Observer<List<LiveChannelBean>> subscriber)
    {
        Observable observable = radioService.getLiveByParam(token, channelPlaceId, limit, offset)
                .map(new HttpResultFunc<List<LiveChannelBean>>());

        toSubscribe(observable, subscriber);
    }

    public void getLivePlace(String token, Observer<List<PlaceBean>> subscriber)
    {
        Observable observable = radioService.getLivePlace(token)
                .map(new HttpResultFunc<List<PlaceBean>>());

        toSubscribe(observable, subscriber);
    }

    public void getHistoryByChannelId(String chinnelId, String token, Observer<List<ChannelSchedulBean>> subscriber)
    {
        Observable observable = radioService.getHistoryByChannelId(chinnelId, token)
                .map(new HttpResultFunc<List<ChannelSchedulBean>>());

        toSubscribe(observable, subscriber);
    }

}
