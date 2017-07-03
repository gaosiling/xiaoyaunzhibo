package com.beicai.xiaoyuanzhibo.api;

import com.beicai.xiaoyuanzhibo.bean.CreatLiveBean;
import com.beicai.xiaoyuanzhibo.bean.LiveBean;
import com.beicai.xiaoyuanzhibo.bean.RigisterBean;
import com.beicai.xiaoyuanzhibo.bean.UpdateLiveBean;
import com.beicai.xiaoyuanzhibo.bean.UserBean;

import okhttp3.FormBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by xiaoyuan on 17/3/10.
 */

public interface HomeApi {
    @POST(UrlConfig.LIVE_HOME)
    Call<LiveBean> get_live(@Body FormBody body);

    @POST(UrlConfig.LIVE_RIGISTER)
    Call<RigisterBean> live_rigister(@Body FormBody body);


    @POST(UrlConfig.LIVE_LOGIN)
    Call<UserBean> live_login(@Body FormBody body);


    @POST(UrlConfig.LIVE_CREATE)
    Call<CreatLiveBean> live_create(@Body FormBody body);

    @POST(UrlConfig.LIVE_STATUS_UPDATE)
    Call<UpdateLiveBean> live_status_update(@Body FormBody body);
}

