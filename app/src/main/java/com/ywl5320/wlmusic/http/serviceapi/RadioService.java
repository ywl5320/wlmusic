package com.ywl5320.wlmusic.http.serviceapi;





import com.ywl5320.wlmusic.beans.ChannelSchedulBean;
import com.ywl5320.wlmusic.beans.HomePageBean;
import com.ywl5320.wlmusic.beans.LiveChannelBean;
import com.ywl5320.wlmusic.beans.LivePageBean;
import com.ywl5320.wlmusic.beans.PlaceBean;
import com.ywl5320.wlmusic.http.httpentity.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ywl on 2016/5/19.
 */
public interface RadioService {

    /**
     * 获取 token
     * @return
     */
    @POST("gettoken")
    Observable<HttpResult<Long>> getToken();


    /**
     * 获取banner
     * @param token
     * @return
     */
    @GET("recommend/getrecommendedpage")
    Observable<HttpResult<HomePageBean>> getHomePage(@Query("token") String token);

    /**
     * 获取banner
     * @param token
     * @return
     */
    @GET("channels/getlivepage")
    Observable<HttpResult<LivePageBean>> getLivePage(@Query("token") String token);

    /**
     * 根据Id获取直播列表
     * @param token
     * @param channelPlaceId
     * @param limit
     * @param offset
     * @return
     */
    @GET("channels/getlivebyparam")
    Observable<HttpResult<List<LiveChannelBean>>> getLiveByParam(@Query("token") String token, @Query("channelPlaceId") String channelPlaceId, @Query("limit") int limit, @Query("offset") int offset);

    /**
     * 获取省市台编号
     * @param token
     * @return
     */
    @GET("channels/getliveplace")
    Observable<HttpResult<List<PlaceBean>>> getLivePlace(@Query("token") String token);

    /**
     * 获取省市台编号
     * @param token
     * @return
     */
    @GET("channels/getchannelschedul/v1")
    Observable<HttpResult<List<ChannelSchedulBean>>> getHistoryByChannelId(@Query("channelId") String channelId, @Query("token") String token);

}
