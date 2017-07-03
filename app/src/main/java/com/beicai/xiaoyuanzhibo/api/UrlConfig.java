package com.beicai.xiaoyuanzhibo.api;


/**
 * 此类是api管理类
 */
public class UrlConfig {

    public static String TestHostUrl;
    public static String TestShareUrl;

    // 0测试环境
    public static final int Test = 0;
    // 1线上环境
    public static final int Online = 1;

    static {
        switch (Online) {
            case Test:
                TestHostUrl = "http://121.42.26.175:14444/";
                TestShareUrl = "http://121.42.26.175:14444/";
                break;
            case Online:
                TestHostUrl = "http://121.42.26.175:14444/";
                TestShareUrl = "http://121.42.26.175:14444/";
                break;
        }
    }

    public final static String UTIL_FILE_UPLOAD = "util/file/upload.json";
    public final static String DOWNLOAD_LIVE = "rtmp://cncplay.bingdou.tv/live/";//拉流地址
    public final static String UP_LIVE = "rtmp://cncpublish.bingdou.tv/live/";//推流地址
    //首页接口
    public final static String LIVE_HOME = "live/find.json";
    public final static String LIVE_RIGISTER = "live/register.json";
    public final static String LIVE_LOGIN = "live/login.json";
    public final static String LIVE_CREATE = "live/create.json";
    public final static String LIVE_STATUS_UPDATE = "live/status/update.json";




}
